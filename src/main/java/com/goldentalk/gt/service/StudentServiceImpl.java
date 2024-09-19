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
        Student student = new Student();
        return saveUpdateStudent(request, student);
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
        response.setInternalId(student.getInternalId());
        response.setFirstName(student.getFirstName());
        response.setMiddleName(student.getMiddleName());
        response.setLastName(student.getLastName());

        Set<String> sectionsIds = student.getSections().stream().map(s -> s.getId().toString()).collect(Collectors.toSet());
        response.setSectionIds(sectionsIds);
    
    /*Set<String> courseIds = student.getCourses().stream().map(c -> c.getCourseId()).collect(Collectors.toSet());
    response.setCourseIds(courseIds);*/

        response.setDob(student.getDob());

        List<PaymentDetailsDTO> paymentDetails = payments.stream().map(pay -> {
            PaymentDetailsDTO details = new PaymentDetailsDTO();

//      details.setCourseId(pay.getCourse().getCourseId());
            details.setPaymentId(pay.getPaymentId());
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

        return saveUpdateStudent(request, student);
    }

    private CreateAndUpdateStudentResponse saveUpdateStudent(CreateAndUpdateStudentRequest request,
                                                             Student student) {
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setMiddleName(request.getMiddleName());
        student.setDob(request.getDob());
        student.setWhatsappNum(request.getWhatsAppNumber());

        Set<Section> sections = sectionRepository.findByIdInAndDeleted(request.getSectionId(), false);

        student.setSections(sections);

        Set<Course> courses = courseRepository.findByIdInAndIsDeleted(request.getCourseIds(), false);

        student.setCourses(courses);

        Address address = new Address();
        address.setStreet(request.getAddress().getStreet());
        address.setCity(request.getAddress().getCity());
        address.setDistrict(request.getAddress().getDistrict());
        address.setProvince(request.getAddress().getProvince());

        student.setAddress(address);

        Student stu = studentRepository.save(student);

        CreateAndUpdateStudentResponse response = new CreateAndUpdateStudentResponse();
        response.setStudentId(stu.getStudentId());
        response.setInternalId(stu.getInternalId());

        return response;
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
