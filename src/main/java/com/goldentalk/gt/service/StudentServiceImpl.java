package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.CreateAndUpdateStudentRequest;
import com.goldentalk.gt.dto.CreateAndUpdateStudentResponse;
import com.goldentalk.gt.dto.PaymentDetailsDTO;
import com.goldentalk.gt.dto.StudentResponseDto;
import com.goldentalk.gt.entity.*;
import com.goldentalk.gt.exception.NotFoundException;
import com.goldentalk.gt.repository.CourseRepository;
import com.goldentalk.gt.repository.SectionRepository;
import com.goldentalk.gt.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private StudentRepository studentRepository;

    private SectionRepository sectionRepository;

    private CourseRepository courseRepository;

    @Override
    public CreateAndUpdateStudentResponse createStudent(CreateAndUpdateStudentRequest request) {
        return saveUpdateStudent(request);
    }

    private CreateAndUpdateStudentResponse saveUpdateStudent(CreateAndUpdateStudentRequest request) {
        Section section = sectionRepository
                .findByIdAndIsDeleted(request.getSectionId(), false)
                .orElseThrow(() -> new NotFoundException("Section Not Found for the given id " + request.getSectionId()));

        courseRepository.findByIdAndSectionAndIsDeleted(request.getCourseId(), section, false)
                .orElseThrow(() -> new NotFoundException("Course id exists under Section Not Found for the given id " + request.getSectionId()));

        Address address = Address.builder()
                .street(request.getAddress().getStreet())
                .city(request.getAddress().getCity())
                .district(request.getAddress().getDistrict())
                .province(request.getAddress().getProvince())
                .build();


//        student.setFirstName(request.getFirstName());
//        student.setLastName(request.getLastName());
//        student.setMiddleName(request.getMiddleName());
////        student.setDob(request.getDob());
//        student.setWhatsappNum(request.getWhatsAppNumber());
//        student.setSections((Set<Section>) section);
//        student.setAddress(address);

        Student student = Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .whatsappNum(request.getWhatsAppNumber())
                .sections(Set.of(section))
                .address(address)
                .build();


//        Set<Course> courses = courseRepository.findByIdInAndIsDeleted(request.getCourseId(), false);

//        student.setCourses(courses);

//        Address address = new Address();
//        address.setStreet(request.getAddress().getStreet());
//        address.setCity(request.getAddress().getCity());
//        address.setDistrict(request.getAddress().getDistrict());
//        address.setProvince(request.getAddress().getProvince());


        Student stu = studentRepository.save(student);

        return CreateAndUpdateStudentResponse.builder()
                .studentId(stu.getStudentId())
                .id(stu.getId())
                .build();

//        CreateAndUpdateStudentResponse response = new CreateAndUpdateStudentResponse();
//        response.setStudentId(stu.getStudentId());
//
//        return response;
    }

    @Override
    public StudentResponseDto retrieveStudents(String studentId) {

        Student student = studentRepository.findByStudentIdAndDeleted(studentId, false);

        if (student == null) {
            throw new NotFoundException("Student not found for the id " + studentId);
        }
        List<Payment> payments = Collections.emptyList();
        if (!student.getCourses().isEmpty()) {
            payments = student.getPayments().stream().collect(Collectors.toList());
        }

        return transformStudentToStudnetResponDto(student, payments);
    }

    private StudentResponseDto transformStudentToStudnetResponDto(Student student, List<Payment> payments) {
        StudentResponseDto response = new StudentResponseDto();

        response.setStudentId(student.getStudentId());
        response.setFirstName(student.getFirstName());
        response.setMiddleName(student.getMiddleName());
        response.setLastName(student.getLastName());

        Set<String> sectionsIds = student.getSections().stream().map(s -> s.getId().toString()).collect(Collectors.toSet());
        response.setSectionIds(sectionsIds);
    
    /*Set<String> courseIds = student.getCourses().stream().map(c -> c.getCourseId()).collect(Collectors.toSet());
    response.setCourseIds(courseIds);*/

//        response.setDob(student.getDob());

        List<PaymentDetailsDTO> paymentDetails = payments.stream().map(pay -> {
            PaymentDetailsDTO details = new PaymentDetailsDTO();

//      details.setCourseId(pay.getCourse().getCourseId());
//            details.setPaymentId(pay.getPaymentId());
            details.setPaymentStatus(pay.getPaymentStatus());
//      details.setPaidAmount(pay.getPaidAmount());
      
      /*List<InstallmentDTO> installments = pay.getInstallments().stream().map(inst -> {
        InstallmentDTO installmentDto = new InstallmentDTO();
        installmentDto.setId(inst.getId());
        installmentDto.setPaymentAmount(inst.getPaymentAmount());
        installmentDto.setPaymentDate(inst.getPaymentDate());
        return installmentDto;
      }).toList();
      
      details.getInstallments().addAll(installments);
*/
            return details;
        }).toList();

        response.getPayments().addAll(paymentDetails);

        return response;

    }

    @Override
    public CreateAndUpdateStudentResponse updateStudent(String studentId,
                                                        CreateAndUpdateStudentRequest request) {

        Assert.hasText(studentId, "Student Id should not be null or Empty");

        Student student = studentRepository.findByStudentIdAndDeleted(studentId, false);

        if (student == null) {
            throw new NotFoundException("Student not found for the id " + studentId);
        }

        return saveUpdateStudent(request);
    }

    @Override
    public boolean deleteStudent(String studentId) {
        Student student = studentRepository.findByStudentIdAndDeleted(studentId, false);

        if (student == null) {
            throw new NotFoundException("Student not found for the id " + studentId);
        }

        student.setDeleted(true);
        return true;
    }
}
