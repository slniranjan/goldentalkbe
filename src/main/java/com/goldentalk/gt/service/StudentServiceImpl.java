package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.*;
import com.goldentalk.gt.entity.*;
import com.goldentalk.gt.entity.enums.PaymentStatus;
import com.goldentalk.gt.exception.LowPaymentException;
import com.goldentalk.gt.exception.NotFoundException;
import com.goldentalk.gt.repository.CourseRepository;
import com.goldentalk.gt.repository.PaymentRepository;
import com.goldentalk.gt.repository.SectionRepository;
import com.goldentalk.gt.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private StudentRepository studentRepository;
    private SectionRepository sectionRepository;
    private CourseRepository courseRepository;
    private PaymentRepository paymentRepository;

    @Value("#{'${app.config.courses}'.split(',')}")
    private List<String> oneMonthCourses;


    @Override
    @Transactional
    public CreateAndUpdateStudentResponse createStudent(CreateStudentRequest request) {
        return createNewStudent(request);
    }

    @Override
    @Transactional
    public StudentResponseDto retrieveStudents(String studentId) {

        Student student = studentRepository.findByStudentIdAndDeleted(studentId, false)
                .orElseThrow(() -> new NotFoundException("Student not found for the id " + studentId));

        List<Payment> payments = Collections.emptyList();
        if (!student.getCourses().isEmpty()) {
            payments = new ArrayList<>(student.getPayments());
        }

        return transformStudentToStudentResponseDto(student, payments);
    }

    @Override
    public CreateAndUpdateStudentResponse updateStudent(String studentId,
                                                        UpdateStudentInfoOnlyRequest request) {
        return changeStudentInfoOnly(studentId, request);
    }

    @Override
    public StudentResponseDto deleteStudent(String studentId) {
        Student student = studentRepository.findByStudentIdAndDeleted(studentId, false)
                .orElseThrow(() -> new NotFoundException("Student not found for the id " + studentId));

        student.setDeleted(true);
        student.getPayments().forEach(payment -> payment.setDeleted(true));
        Student savedStudent = studentRepository.save(student);

        return StudentResponseDto.builder()
                .studentId(savedStudent.getStudentId())
                .firstName(savedStudent.getFirstName())
                .middleName(savedStudent.getMiddleName())
                .lastName(savedStudent.getLastName())
                .course(savedStudent.getCourses().stream().map(course -> {
                    return CourseResponseDto.builder().id(course.getId()).courseName(course.getName()).build();
                }).collect(Collectors.toSet()))
//                .course(savedStudent.getCourses().stream().map(Course::getName).collect(Collectors.toSet()))
                .address(savedStudent.getAddress())
                .build();
    }

    @Transactional
    @Override
    public StudentResponseDto updateSecondPayment(String studentId, Integer courseId, Double payment) {

        Student student = studentRepository.findStudentByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new NotFoundException("Student " + studentId + " not registered for the course " + courseId));

        Course course = courseRepository.findActiveCourseById(courseId, false)
                .orElseThrow(() -> new NotFoundException("Course not found for the id " + courseId));

        Payment existPayment = student.getPayments().stream()
                .filter(p ->
                        Objects.equals(p.getCourse().getId(), courseId)
                ).findFirst().orElseThrow(() -> new NotFoundException("Valid payment details not found for the student " + studentId));

        if (course.getFee() <= (payment + existPayment.getFirstPaymentAmount())) {
            Payment processingPayment = paymentRepository.findById(existPayment.getPaymentId()).orElseThrow();
            processingPayment.setSecondPaymentAmount(payment);
            processingPayment.setPaymentStatus(PaymentStatus.COMPLETED);

            paymentRepository.save(processingPayment);
            List<Payment> payments = studentRepository.findStudentByStudentIdAndCourseId(studentId, courseId).orElseThrow().getPayments().stream().toList();
            return transformStudentToStudentResponseDto(student, payments);
        } else {
            throw new LowPaymentException("Remaining full balance of " + (course.getFee() - existPayment.getFirstPaymentAmount()) + " is required for the last payment.");
        }
    }

    /**
     * Upcoming payments consider only within next 7 days
     *
     * @return
     */
    @Override
    public List<NotificationDto> getUpcomingPayments() {
        LocalDateTime now = LocalDateTime.now();
        List<Payment> upcomingPayments = paymentRepository.findUpcomingPayments(now, now.plusDays(7), PaymentStatus.PENDING, false);

        return getNotificationDtos(upcomingPayments);
    }

    @Override
    public List<NotificationDto> getDelayPayments() {
        List<Payment> delayPayments = paymentRepository.findByNextPaymentDateBeforeAndDeleted(LocalDateTime.now(), false);
        return getNotificationDtos(delayPayments);
    }

    @Override
    public List<StudentResponseDto> getAllStudents(Boolean deleted) {
        List<Student> students = studentRepository.findAllActiveOrDeleted(deleted);
        return students.stream()
                .map(student -> transformStudentToStudentResponseDto(student, student.getPayments().stream().toList())).toList();
    }

    @Override
    @Transactional
    public CreateAndUpdateStudentResponse addStudentToNewCourse(String studentId, Integer courseId, PaymentDetailsDTO request) {
        Student student = studentRepository.findByStudentIdAndDeleted(studentId, false)
                .orElseThrow(() -> new NotFoundException(studentId + " is not registered"));

        Optional<Student> assignedStudent = studentRepository.findStudentByStudentIdAndCourseId(studentId, courseId);
//                .orElseThrow(() -> new IllegalArgumentException(studentId + " has already registered for the course " + courseId));

        if (assignedStudent.isEmpty()) {
            Course course = courseRepository.findActiveCourseById(courseId, false)
                    .orElseThrow(() -> new NotFoundException("Course not found for the given id " + courseId));

            /*Validating payment details for the selected course before save the student*/

            Payment payment = new Payment();
            String message = "";
            PaymentStatus paymentStatus = null;
            if (course.getInstallment()) {
                if (course.getFee() <= request.getFirstPaymentAmount()) {
                    paymentStatus = PaymentStatus.COMPLETED;
                    message = "Full payment done. Student successfully registered!";
                } else {
                    paymentStatus = PaymentStatus.PENDING;
                    message = "Partial payment done. Student successfully registered!";
                }
            } else {
                if (course.getFee() <= request.getFirstPaymentAmount()) {
                    paymentStatus = PaymentStatus.COMPLETED;
                    message = "Full payment done. Student successfully registered!";
                } else if (course.getFee() > request.getFirstPaymentAmount()) {
                    throw new LowPaymentException("Full payment of " + course.getFee() + " is required to register this course");
                }
            }

            Set<Course> courses = student.getCourses();
            courses.add(course);

            student.setCourses(courses);

            Student stu = studentRepository.save(student);

            payment = Payment.builder()
                    .paymentStatus(paymentStatus)
                    .firstPaymentAmount(request.getFirstPaymentAmount())
                    .student(stu)
                    .course(course)
                    .build();

            Payment save = paymentRepository.save(payment);

            return CreateAndUpdateStudentResponse.builder()
                    .studentId(stu.getStudentId())
                    .id(stu.getId())
                    .message(message)
                    .build();
        } else {
            throw new IllegalArgumentException(studentId + " has already registered for the course " + courseId);
        }
    }

    private List<NotificationDto> getNotificationDtos(List<Payment> upcomingPayments) {
        return upcomingPayments.stream().map(payment -> {
            var studentResponseDto = StudentResponseDto.builder()
                    .studentId(payment.getStudent().getStudentId())
                    .firstName(payment.getStudent().getFirstName())
                    .middleName(payment.getStudent().getMiddleName())
                    .lastName(payment.getStudent().getLastName())
                    .whatsAppNum(payment.getStudent().getWhatsappNum())
                    .address(payment.getStudent().getAddress())
                    .build();

            return NotificationDto.builder()
                    .firstPaymentAmount(payment.getFirstPaymentAmount())
                    .firstPaymentDate(payment.getFirstPaymentDate())
                    .nextPaymentDate(payment.getNextPaymentDate())
                    .paymentStatus(payment.getPaymentStatus())
                    .studentResponseDto(studentResponseDto)
                    .build();
        }).toList();
    }

    private StudentResponseDto transformStudentToStudentResponseDto(Student student, List<Payment> payments) {

        List<PaymentDetailsDTO> paymentDetails = payments.stream().map(p -> PaymentDetailsDTO.builder()
                .firstPaymentAmount(p.getFirstPaymentAmount())
                .secondPaymentAmount(p.getSecondPaymentAmount())
                .paymentStatus(p.getPaymentStatus())
                .build()
        ).toList();

        Set<SectionResponseDTO> sectionResponseDTOS = student.getSections().stream()
                .map(section -> {
                    return SectionResponseDTO.builder()
                            .id(section.getId())
                            .sectionName(section.getSectionName())
                            .build();
                }).collect(Collectors.toSet());

        Set<CourseResponseDto> courseResponseDtos = student.getCourses().stream()
                .map(course -> {
                    return CourseResponseDto.builder()
                            .id(course.getId())
                            .category(course.getCategory())
                            .courseName(course.getName())
                            .courseFee(course.getFee())
                            .build();
                }).collect(Collectors.toSet());

        return StudentResponseDto.builder()
                .studentId(student.getStudentId())
                .firstName(student.getFirstName())
                .middleName(student.getMiddleName())
                .lastName(student.getLastName())
                .whatsAppNum(student.getWhatsappNum())
                .nic(student.getNic())
                .email(student.getEmail())
                .address(student.getAddress())
                .section(sectionResponseDTOS)
                .course(courseResponseDtos)
                .payments(paymentDetails)
                .build();
    }

    private CreateAndUpdateStudentResponse createNewStudent(CreateStudentRequest request) {

        Course course = courseRepository.findActiveCourseById(request.getCourseId(), false)
                .orElseThrow(() -> new NotFoundException("Course not found for the given id " + request.getCourseId()));

        Section section = sectionRepository
                .findByIdAndDeleted(request.getSectionId(), false)
                .orElseThrow(() -> new NotFoundException("Section Not Found for the given id " + request.getSectionId()));

        courseRepository.findByIdAndSectionAndIsDeleted(request.getCourseId(), section, false)
                .orElseThrow(() -> new NotFoundException("Given course " + course.getName() +
                        " doesn't include in " + section.getSectionName() + " section"));

        /*Validating payment details for the selected course before save the student*/
        String message = "";
        Student stu = new Student();
        PaymentStatus paymentStatus = null;
        LocalDateTime nextPaymentDate = null;

        String fullPayMsg = "Full payment done. Student successfully registered! ";

        if (request.getEarlyBird()) {
            double discountedPrice = course.getFee() - course.getDiscount();
            if (discountedPrice == request.getPayment().getFirstPaymentAmount()) {
                paymentStatus = PaymentStatus.COMPLETED;
                message = fullPayMsg;
            } else if (discountedPrice < request.getPayment().getFirstPaymentAmount()) {
                paymentStatus = PaymentStatus.COMPLETED;
                double balance = request.getPayment().getFirstPaymentAmount() - discountedPrice;
                request.getPayment().setFirstPaymentAmount(discountedPrice);
                message = "Full payment done. The student has Rs." + balance + " amount as a balance. Student successfully registered!";
            } else {
                throw new LowPaymentException("Full payment of Rs." + discountedPrice + " is required to register under the EarlyBird offer");
            }
        } else {
            if (course.getInstallment()) {
                if (course.getFee() == request.getPayment().getFirstPaymentAmount()) {
                    paymentStatus = PaymentStatus.COMPLETED;
                    message = fullPayMsg;
                } else if (course.getFee() < request.getPayment().getFirstPaymentAmount()) {
                    paymentStatus = PaymentStatus.COMPLETED;
                    double balance = request.getPayment().getFirstPaymentAmount() - course.getFee();
                    message = "Full payment done. The student has Rs." + balance + " amount as a balance. Student successfully registered!";
                } else {
                    paymentStatus = PaymentStatus.PENDING;
                    message = "Partial payment done. Student successfully registered!";

                    /*set next payment date according to course duration*/
                    if (oneMonthCourses.contains(course.getName())) {
                        nextPaymentDate = LocalDateTime.now().plusWeeks(2);
                    } else {
                        nextPaymentDate = LocalDateTime.now().plusMonths(1);
                    }
                }
            } else {
                    if (course.getFee() == request.getPayment().getFirstPaymentAmount()) {
                        paymentStatus = PaymentStatus.COMPLETED;
                        message = fullPayMsg;
                    } else if (course.getFee() < request.getPayment().getFirstPaymentAmount()) {
                        paymentStatus = PaymentStatus.COMPLETED;
                        double balance = request.getPayment().getFirstPaymentAmount() - course.getFee();
                        request.getPayment().setFirstPaymentAmount(course.getFee());
                        message = "Full payment done. The student has Rs." + balance + " amount as a balance. Student successfully registered!";
                    } else if (course.getFee() > request.getPayment().getFirstPaymentAmount()) {
                        throw new LowPaymentException("Full payment of Rs." + course.getFee() + " is required to register to this course");
                    }
            }
        }

        Address address = Address.builder()
                .street(request.getAddress().getStreet())
                .city(request.getAddress().getCity())
                .district(request.getAddress().getDistrict())
                .province(request.getAddress().getProvince())
                .build();

        Student student = Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .whatsappNum(request.getWhatsAppNumber())
                .nic(request.getNic())
                .email(request.getEmail())
                .earlyBird(request.getEarlyBird())
                .sections(Set.of(section))
                .address(address)
                .courses(Set.of(course))
                .build();

        stu = studentRepository.save(student);

        Payment payment = Payment.builder()
                .paymentStatus(paymentStatus)
                .firstPaymentAmount(request.getPayment().getFirstPaymentAmount())
                .nextPaymentDate(nextPaymentDate)
                .student(stu)
                .course(course)
                .build();

        Payment save = paymentRepository.save(payment);

        return CreateAndUpdateStudentResponse.builder()
                .studentId(stu.getStudentId())
                .id(stu.getId())
                .message(message)
                .build();

    }

    private CreateAndUpdateStudentResponse changeStudentInfoOnly(String id, UpdateStudentInfoOnlyRequest request) {

        Student student = studentRepository.findByStudentIdAndDeleted(id, false)
                .orElseThrow(() -> new NotFoundException("Student ID: " + id + " not found"));

        Address address = student.getAddress();
        address.setStreet(request.getAddress().getStreet());
        address.setCity(request.getAddress().getCity());
        address.setDistrict(request.getAddress().getDistrict());
        address.setProvince(request.getAddress().getProvince());

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setMiddleName(request.getMiddleName());
        student.setWhatsappNum(request.getWhatsAppNumber());
        student.setNic(request.getNic());
        student.setEmail(request.getEmail());
        student.setAddress(address);

        Student stu = studentRepository.save(student);

        return CreateAndUpdateStudentResponse.builder()
                .studentId(stu.getStudentId())
                .id(stu.getId())
                .message("Student successfully updated")
                .build();
    }

}
