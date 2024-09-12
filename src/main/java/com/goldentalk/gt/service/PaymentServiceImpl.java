package com.goldentalk.gt.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.goldentalk.gt.dto.PaymentRequest;
import com.goldentalk.gt.dto.PaymentResponse;
import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Installment;
import com.goldentalk.gt.entity.Payment;
import com.goldentalk.gt.entity.Student;
import com.goldentalk.gt.entity.enums.PaymentStatus;
import com.goldentalk.gt.exception.CourseNotFoundException;
import com.goldentalk.gt.exception.StudentNotFoundException;
import com.goldentalk.gt.repository.CourseRepository;
import com.goldentalk.gt.repository.PaymentRepository;
import com.goldentalk.gt.repository.StudentRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService{
 
  private static final Logger loger = LoggerFactory.getLogger(PaymentServiceImpl.class);
  
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

    payment = paymentRepository.findByCourseAndStudent(course, student);
    
    if(payment != null) {
      saveExistingPayment(request, course, payment);
    } else {
      payment = saveNewPyament(request, payment, student, course);
    }

    String installmentId = payment.getInstallments().stream()
        .sorted(Comparator.comparingInt(Installment::getId).reversed())
        .map(i -> i.getId().toString()).findFirst().get();
    
    return new PaymentResponse(payment.getPaymentId().toString(), installmentId , request.getPaymentAmount());
  }

  private Payment saveNewPyament(PaymentRequest request, Payment payment, Student student,
      Course course) {
    payment = new Payment();
    
    double courseAmount = course.getAmount();
    double requestAmount = request.getPaymentAmount();
    double minimumInstallmentAmount = course.getAmount() / course.getInstallmentCount();;
    double installmentAmount = 0;
    
    if(!course.isInstallment() || !request.isInstallment()) {
      if(courseAmount != requestAmount) {
        throw new IllegalArgumentException(courseAmount > requestAmount
            ? "Payment amount is not enough for full payment"
            : "Overpaying");
      }
      payment.setPaymentStatus(PaymentStatus.COMPLETED);
      payment.setInstallmentAmount(requestAmount);  
      
      
      
    } else if(courseAmount == requestAmount){
      payment.setPaymentStatus(PaymentStatus.COMPLETED);
      payment.setInstallmentAmount(requestAmount);    
    } else {
      if (courseAmount < requestAmount) {
        throw new IllegalArgumentException("Overpaying");
      }
      
      validateInstallmentCount(request, course);  // Extracted validation logic for installment count

      // Calculate installment amount
      if (request.getInstallmentCount() == course.getInstallmentCount()) {
          installmentAmount = minimumInstallmentAmount;
      } else {
          installmentAmount = courseAmount / request.getInstallmentCount();
      }
      
      payment.setInstallmentAmount(installmentAmount);
      payment.setPaymentStatus(PaymentStatus.PENDING);
      
      payment.setMinimumInstallmentAmount(minimumInstallmentAmount); 

      // Check for valid installment payment amounts
      validateInstallmentAmount(installmentAmount, requestAmount, minimumInstallmentAmount);
      
      payment.setRemainigInstallmentCount(request.getInstallmentCount() - 1);
    }
    


    // Set additional payment properties
    payment.setCourse(course);
    payment.setStudent(student);
    payment.setPaidAmount(requestAmount);

    // Create and add the first installment
    Installment installment = new Installment();
    installment.setPayment(payment);
    installment.setPaymentAmount(requestAmount);
    installment.setPaymentDate(LocalDateTime.now());

    payment.setInstallments(Set.of(installment));

    return paymentRepository.save(payment);
    
  }
  
  private void validateInstallmentCount(PaymentRequest request, Course course) {
    int requestInstallmentCount = request.getInstallmentCount();
    int courseInstallmentCount = course.getInstallmentCount();
    
    if (requestInstallmentCount > courseInstallmentCount || requestInstallmentCount <= 0) {
        throw new IllegalArgumentException("Invalid installment count");
    }
  }
  
  private void validateInstallmentAmount(double installmentAmount, double requestAmount, double minimumInstallmentAmount) {
    if (installmentAmount > requestAmount) {
        throw new IllegalArgumentException("Not enough installment amount, you need to pay at least " + minimumInstallmentAmount);
    } else if (installmentAmount < requestAmount) {
        throw new IllegalArgumentException("Please enter the correct installment amount: " + installmentAmount);
    }
}

  private Payment saveExistingPayment(PaymentRequest request, Course course,
      Payment existingPayment) {
    
    double requestAmount = request.getPaymentAmount();
    
    if(PaymentStatus.COMPLETED.equals(existingPayment.getPaymentStatus())) {
      throw new IllegalArgumentException("Payment already completed");
    }
    
    double remaingAmount = course.getAmount() - existingPayment.getPaidAmount();
    
    int remaiingIntallmentCount = course.getInstallmentCount() - existingPayment.getInstallments().size();
    
    if(remaiingIntallmentCount == 1 ) {
      if (requestAmount != remaingAmount) {
        throw new IllegalArgumentException(requestAmount < remaingAmount ? 
            "Not enough payment amount" : "Overcharging");
      }
      
      existingPayment.setPaidAmount(existingPayment.getPaidAmount() + remaingAmount);
      existingPayment.setPaymentStatus(PaymentStatus.COMPLETED);
      existingPayment.setRemainigInstallmentCount(0);
    } else if(remaiingIntallmentCount > 1) {
      if(!request.isInstallment()) {
        if(requestAmount !=  remaingAmount) {
          throw new IllegalArgumentException(remaingAmount > requestAmount ? "payment amount is not enough for full payment" : "over paying");
        }
        
        existingPayment.setPaidAmount(existingPayment.getPaidAmount() + remaingAmount);
        existingPayment.setPaymentStatus(PaymentStatus.COMPLETED);
        existingPayment.setRemainigInstallmentCount(0);
      } else {
        if(remaingAmount < requestAmount) {
          throw new IllegalArgumentException( "over paying");
        }
        if(requestAmount != existingPayment.getInstallmentAmount()) {
          throw new IllegalArgumentException(requestAmount < existingPayment.getInstallmentAmount() ? "Pay the installment amount : " + existingPayment.getInstallmentAmount() : 
              "over paying");
        }
        
        existingPayment.setRemainigInstallmentCount(--remaiingIntallmentCount);
        existingPayment.setPaidAmount( existingPayment.getPaidAmount() + existingPayment.getInstallmentAmount());
        existingPayment.setPaymentStatus(PaymentStatus.PENDING);
      }
    }
    
    Installment installment = new Installment();
    installment.setPayment(existingPayment);
    installment.setPaymentAmount(request.getPaymentAmount());
    installment.setPaymentDate(LocalDateTime.now());
    
    existingPayment.getInstallments().add(installment);

    return paymentRepository.save(existingPayment);
  }

}
