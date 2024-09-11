package com.goldentalk.gt.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.goldentalk.gt.dto.PaymentRequest;
import com.goldentalk.gt.dto.PaymentResponse;
import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Installment;
import com.goldentalk.gt.entity.Payment;
import com.goldentalk.gt.entity.Student;
import com.goldentalk.gt.exception.CourseNotFoundException;
import com.goldentalk.gt.exception.StudentNotFoundException;
import com.goldentalk.gt.repository.CourseRepository;
import com.goldentalk.gt.repository.PaymentRepository;
import com.goldentalk.gt.repository.StudentRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService{
 
  
  private StudentRepository studentRepository;
  
  private CourseRepository courseRepository;
  
  private PaymentRepository paymentRepository;

  @Override
  public PaymentResponse savePayment(PaymentRequest request) {
    Payment payment = new Payment();

    Student student = studentRepository.findByStudentIdAndDeleted(request.getStudentId(), false);
    
    if(student == null) {
      throw new StudentNotFoundException("Student not found for the id " + request.getStudentId());
    }
    
    Course course = courseRepository.findByCourseIdAndIsDeleted(request.getCourseId(), false);

    if(course == null) {
      throw new CourseNotFoundException("Course not found for the id : " + request.getCourseId());
    }

//    payment = paymentRepository.findByCourseIdAndStudentId(student.getId(), course.getId());
    payment = paymentRepository.findByCourseAndStudent(course, student);
    
    if(payment != null) {
      saveExistingPayment(request, course, payment);
    } else {
      
        saveNewPyament(request, payment, student, course);
        
        
    }

    return new PaymentResponse("123123", request.getPaymentAmount());
  }

  private void saveNewPyament(PaymentRequest request, Payment payment, Student student,
      Course course) {
    
    System.out.println("adding new path");
    payment = new Payment();
    
    if(!course.isInstallment()) {
      // request amount should be full amount
      if(course.getAmount() > request.getPaymentAmount()) {
        throw new IllegalArgumentException("payment should be done in full");
      }
      
    } else {
      if(request.getInstallmentCount() > course.getInstallmentCount()) {
        throw new IllegalArgumentException("wrong installment count");
      }
      
      double installmentAmount = course.getAmount() / course.getInstallmentCount();
      
      if(installmentAmount > request.getPaymentAmount()) {
        throw new IllegalArgumentException("Not enough installment amount");
      }
      
      payment.setCourse(course);
      payment.setStudent(student);
      payment.setPaymentStatus("PENDING");
      payment.setPaidAmount(request.getPaymentAmount());
      payment.setInstallmentAmount(course.getAmount() / course.getInstallmentCount());
      
      
      // save payment
      
      Installment installment = new Installment();
      installment.setPayment(payment);
      installment.setPaymentAmount(request.getPaymentAmount());
      installment.setPaymentDate(LocalDateTime.now());
      
      paymentRepository.save(payment);
    }
  }

  private void saveExistingPayment(PaymentRequest request, Course course,
      Payment existingPayment) {
    
    if("Completed".equals(existingPayment.getPaymentStatus())) {
      throw new IllegalArgumentException("Payment already completed");
    }
    
    double remaingAmount = course.getAmount() - existingPayment.getPaidAmount();
    int remaiingIntallmentCount = course.getInstallmentCount() - existingPayment.getInstallments().size();
    
    if(remaiingIntallmentCount == 1 && request.getPaymentAmount() < remaingAmount) {
      throw new IllegalArgumentException("Not enough payment amount");
    } else if(remaiingIntallmentCount == 1 && request.getPaymentAmount() > remaingAmount) {
      throw new IllegalArgumentException("Over charging"); 
    }
    
    double paidAmount = existingPayment.getPaidAmount() + request.getPaymentAmount();
    
    existingPayment.setRemainigInstallmentCount(remaiingIntallmentCount++);
    existingPayment.setPaidAmount(paidAmount);
    existingPayment.setPaymentStatus(paidAmount == course.getAmount() ? "completed" : "pending");
    
    Installment installment1 = new Installment();
    installment1.setPayment(existingPayment);
    installment1.setPaymentAmount(request.getPaymentAmount());
    installment1.setPaymentDate(LocalDateTime.now());
    
    existingPayment.getInstallments().add(installment1);

    paymentRepository.save(existingPayment);
  }

}
