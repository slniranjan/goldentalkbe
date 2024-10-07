package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.CreateAndUpdateStudentRequest;
import com.goldentalk.gt.dto.CreateAndUpdateStudentResponse;
import com.goldentalk.gt.dto.PaymentDetailsDTO;
import com.goldentalk.gt.dto.StudentResponseDto;
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

        Course course = courseRepository.findByIdAndIsDeleted(request.getCourseId(), false)
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
        if (course.isInstallment()) {
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

    private StudentResponseDto transformStudentToStudentResponseDto(Student student, List<Payment> payments) {
        StudentResponseDto response = new StudentResponseDto();

        response.setStudentId(student.getStudentId());
        response.setFirstName(student.getFirstName());
        response.setMiddleName(student.getMiddleName());
        response.setLastName(student.getLastName());
        response.setWhatsAppNum(student.getWhatsappNum());

        response.setAddress(student.getAddress());

        Set<String> sectionsIds = student.getSections().stream().map(s -> s.getId().toString()).collect(Collectors.toSet());
        response.setSection(sectionsIds);

        Set<String> courseIds = student.getCourses().stream().map(c -> c.getId().toString()).collect(Collectors.toSet());
        response.setCourse(courseIds);

        List<PaymentDetailsDTO> paymentDetails = payments.stream().map(p -> PaymentDetailsDTO.builder()
                .firstPaymentAmount(p.getFirstPaymentAmount())
                .secondPaymentAmount(p.getSecondPaymentAmount())
                .paymentStatus(p.getPaymentStatus())
                .build()
        ).toList();

//        response.setDob(student.getDob());

//        List<PaymentDetailsDTO> paymentDetails = payments.stream().map(pay -> {
//            PaymentDetailsDTO details = new PaymentDetailsDTO();
//
//      details.setCourseId(pay.getCourse().getCourseId());
//            details.setPaymentId(pay.getPaymentId());
//            details.setPaymentStatus(pay.getPaymentStatus());
//      details.setPaidAmount(pay.getPaidAmount());
//      
//      List<InstallmentDTO> installments = pay.getInstallments().stream().map(inst -> {
//        InstallmentDTO installmentDto = new InstallmentDTO();
//        installmentDto.setId(inst.getId());
//        installmentDto.setPaymentAmount(inst.getPaymentAmount());
//        installmentDto.setPaymentDate(inst.getPaymentDate());
//        return installmentDto;
//      }).toList();
//      
//      details.getInstallments().addAll(installments);

//            return details;
//        }).toList();

        response.getPayments().addAll(paymentDetails);

        return response;

    }

    @Override
    public CreateAndUpdateStudentResponse updateStudent(String studentId,
                                                        CreateAndUpdateStudentRequest request) {

        /*Assert.hasText(studentId, "Student Id should not be null or Empty");

        Student student = studentRepository.findByStudentIdAndDeleted(studentId, false);

        if (student == null) {
            throw new NotFoundException("Student not found for the id " + studentId);
        }*/

        return saveUpdateStudent(request);
    }

    @Override
    public boolean deleteStudent(String studentId) {
        /*Student student = studentRepository.findByStudentIdAndDeleted(studentId, false);

        if (student == null) {
            throw new NotFoundException("Student not found for the id " + studentId);
        }

        student.setDeleted(true);*/
        return true;
    }

    @Transactional
    @Override
    public StudentResponseDto updateSecondPayment(String studentId, Integer courseId, Double payment) {

        Student student = studentRepository.findStudentByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new NotFoundException("Student " + studentId + " not registered for the course " + courseId));

        Course course = courseRepository.findByIdAndIsDeleted(courseId, false)
                .orElseThrow(() -> new NotFoundException("Course not found for the id " + courseId));

        Payment existPayment = student.getPayments().stream()
                .filter(p ->
                        Objects.equals(p.getCourse().getId(), courseId)
                ).findFirst().get();

        if (course.getFee() < (payment + existPayment.getFirstPaymentAmount())) {
            int status = paymentRepository.updateSecondPaymentAmount(existPayment.getPaymentId(), payment, PaymentStatus.COMPLETED);

            if (status == 1) {
                List<Payment> payments = studentRepository.findStudentByStudentIdAndCourseId(studentId, courseId).get().getPayments().stream().toList();
                return transformStudentToStudentResponseDto(student, payments);
            }
        } else {
            throw new LowPaymentException("Remaining full balance of " + (course.getFee() - existPayment.getFirstPaymentAmount()) + " is required for the last payment.");
        }

        return new StudentResponseDto();
    }
}
