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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private StudentRepository studentRepository;
    private SectionRepository sectionRepository;
    private CourseRepository courseRepository;
    private PaymentRepository paymentRepository;


    @Override
    @Transactional
    public CreateAndUpdateStudentResponse createStudent(CreateAndUpdateStudentRequest request) {
        return saveUpdateStudent(request);
    }

    private CreateAndUpdateStudentResponse saveUpdateStudent(CreateAndUpdateStudentRequest request) {

        Course course = courseRepository.findActiveCourseById(request.getCourseId(), false)
                .orElseThrow(() -> new NotFoundException("Course not found for the given id " + request.getCourseId()));

        Section section = sectionRepository
                .findByIdAndDeleted(request.getSectionId(), false)
                .orElseThrow(() -> new NotFoundException("Section Not Found for the given id " + request.getSectionId()));

        courseRepository.findByIdAndSectionAndIsDeleted(request.getCourseId(), section, false)
                .orElseThrow(() -> new NotFoundException("Given course " + course.getName() +
                        " doesn't include in " + section.getSectionName() + " section"));

        /*Validating payment details for the selected course before save the student*/

        Payment payment = new Payment();
        String message = "";
        Student stu = new Student();
        PaymentStatus paymentStatus = null;
        if (course.getInstallment()) {
            if (course.getFee() <= request.getPayment().getFirstPaymentAmount()) {
                paymentStatus = PaymentStatus.COMPLETED;
                message = "Full payment done. Student successfully registered!";
            } else {
                paymentStatus = PaymentStatus.PENDING;
                message = "Partial payment done. Student successfully registered!";
            }
        } else {
            if (course.getFee() <= request.getPayment().getFirstPaymentAmount()) {
                paymentStatus = PaymentStatus.COMPLETED;
                message = "Full payment done. Student successfully registered!";
            } else if (course.getFee() > request.getPayment().getFirstPaymentAmount()) {
                throw new LowPaymentException("Full payment of " + course.getFee() + " is required to register this course");
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
                .sections(Set.of(section))
                .address(address)
                .courses(Set.of(course))
                .build();

        stu = studentRepository.save(student);

        payment = Payment.builder()
                .paymentStatus(paymentStatus)
                .firstPaymentAmount(request.getPayment().getFirstPaymentAmount())
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

    @Override
    @Transactional
    public StudentResponseDto retrieveStudents(String studentId) {

        Student student = studentRepository.findByStudentIdAndDeleted(studentId, false)
                .orElseThrow(() -> new NotFoundException("Student not found for the id " + studentId));

        List<Payment> payments = Collections.emptyList();
        if (!student.getCourses().isEmpty()) {
            payments = student.getPayments().stream().collect(Collectors.toList());
        }

        return transformStudentToStudentResponseDto(student, payments);
    }

    @Override
    public CreateAndUpdateStudentResponse updateStudent(String studentId,
                                                        CreateAndUpdateStudentRequest request) {
        return saveUpdateStudent(request);
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
                .course(savedStudent.getCourses().stream().map(Course::getName).collect(Collectors.toSet()))
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

        return StudentResponseDto.builder()
                .studentId(student.getStudentId())
                .firstName(student.getFirstName())
                .middleName(student.getMiddleName())
                .lastName(student.getLastName())
                .whatsAppNum(student.getWhatsappNum())
                .address(student.getAddress())
                .section(student.getSections().stream().map(s -> s.getId().toString()).collect(Collectors.toSet()))
                .course(student.getCourses().stream().map(c -> c.getId().toString()).collect(Collectors.toSet()))
                .payments(paymentDetails)
                .build();
    }
}
