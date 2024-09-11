package com.goldentalk.gt.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.goldentalk.gt.dto.CreateStudentRequestDto;
import com.goldentalk.gt.dto.CreateStudentResponseDto;
import com.goldentalk.gt.dto.InstallmentDTO;
import com.goldentalk.gt.dto.PaymentDetailsDTO;
import com.goldentalk.gt.dto.StudentResponseDto;
import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Payment;
import com.goldentalk.gt.entity.Section;
import com.goldentalk.gt.entity.Student;
import com.goldentalk.gt.exception.StudentNotFoundException;
import com.goldentalk.gt.repository.CourseRepository;
import com.goldentalk.gt.repository.PaymentRepository;
import com.goldentalk.gt.repository.SectionRepository;
import com.goldentalk.gt.repository.StudentRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
  
  private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
  
  private StudentRepository studentRepository;
  
  private SectionRepository sectionRepository;
  
  private CourseRepository courseRepository;
  
  private PaymentRepository paymentRepository;

  @Override
  public CreateStudentResponseDto createStudent(CreateStudentRequestDto request) {
    Student student = new Student();
    student.setFirstName(request.getFirstName());
    student.setLastName(request.getLastName());
    student.setMiddleName(request.getMiddleName());
    student.setDob(request.getDob());
    student.setWhatsappNum(request.getWhatsAppNumber());
    
    Set<Section> sections = sectionRepository.findByIdInAndDeleted(request.getSectionId(), false);
    
    student.setSections(sections);
    
    Set<Course> courses = courseRepository.findByIdInAndIsDeleted(request.getCourseIds(), false);
    
    student.setCourses(courses);
    student.setAddress(request.getAddress());
   
    Student stu = studentRepository.save(student);
    
    CreateStudentResponseDto response = new CreateStudentResponseDto();
    response.setStudentId(stu.getStudentId());
    
    return response;
  }

  @Override
  public StudentResponseDto retrieveStudents(String studentId) {
    
    Student student = studentRepository.findByStudentIdAndDeleted(studentId, false);
    
    if(student == null) {
      throw new StudentNotFoundException("Student not found for the id " + studentId);
    }
    List<Payment> payments = Collections.emptyList();
    if(!student.getCourses().isEmpty()) {
      List<Integer> courseIds = student.getCourses().stream().map(c -> c.getId()).toList();
      
      Set<Course> courses = courseRepository.findByIdInAndIsDeleted(courseIds, false);
      
      payments = paymentRepository.findByStudentAndCourseIn(student, courses);
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
    
    Set<String> courseIds = student.getCourses().stream().map(c -> c.getCourseId()).collect(Collectors.toSet());
    response.setCourseIds(courseIds);
    
    response.setDob(student.getDob());
    
    List<PaymentDetailsDTO> paymentDetails = payments.stream().map(pay -> {
      PaymentDetailsDTO details = new PaymentDetailsDTO();
      
      details.setCourseId(pay.getCourse().getCourseId());
      details.setPaymentId(pay.getPaymentId());
      details.setPaymentStatus(pay.getPaymentStatus());
      
      List<InstallmentDTO> installments = pay.getInstallments().stream().map(inst -> {
        InstallmentDTO installmentDto = new InstallmentDTO();
        installmentDto.setId(inst.getId());
        installmentDto.setPaymentAmount(inst.getPaymentAmount());
        installmentDto.setPaymentDate(inst.getPaymentDate());
        return installmentDto;
      }).toList();
      
      details.getInstallments().addAll(installments);

      return details;
    }).toList();
    
    response.getPayments().addAll(paymentDetails);
    
    return response;
    
  }
}
