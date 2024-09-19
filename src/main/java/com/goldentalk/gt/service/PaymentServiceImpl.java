package com.goldentalk.gt.service;

import com.goldentalk.gt.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.goldentalk.gt.dto.PaymentRequest;
import com.goldentalk.gt.dto.PaymentResponse;
import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Payment;
import com.goldentalk.gt.entity.Student;
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
      throw new NotFoundException("Student not found for the id " + request.getStudentId());
    }
    
    Course course = courseRepository.findByIdAndIsDeleted(request.getId(), false);

    if(course == null) {
      throw new NotFoundException("Course not found for the id : " + request.getId());
    }

    payment = paymentRepository.findByCourseAndStudent(course, student);
    
    if(payment != null) {
//      payment = saveExistingPayment(request, course, payment);
    } else {
//      payment = saveNewPyament(request, payment, student, course);
    }

//    String installmentId = payment.getInstallments().stream()
//        .sorted(Comparator.comparingInt(Installment::getId).reversed())
//        .map(i -> i.getId().toString()).findFirst().get();
    // TODO
    return new PaymentResponse(payment.getPaymentId().toString(), "installmentId" , request.getPaymentAmount());
  }

  /*private Payment saveNewPyament(PaymentRequest request, Payment payment, Student student,
      Course course) {
    payment = new Payment();
    
    double courseAmount = course.getAmount();
    double requestAmount = request.getPaymentAmount();
    double minimumInstallmentAmount = course.getAmount() / course.getAllowedInstallment();
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
      installmentPaymentForNewPayments(request, payment, course, courseAmount, requestAmount,
          minimumInstallmentAmount);
    }
    // Set additional payment properties
    payment.setCourse(course);
    payment.setStudent(student);
    payment.setPaidAmount(requestAmount);

    saveInstallment(true, payment, requestAmount);
    
    return paymentRepository.save(payment);
    
  }*/
  
  /*private void saveInstallment(boolean newPayment, Payment payment, double payingAmount) {
    Installment installment = new Installment();
    installment.setPayment(payment);
    installment.setPaymentAmount(payingAmount);
    installment.setPaymentDate(LocalDateTime.now());

    if(newPayment) {
      payment.setInstallments(Set.of(installment));
    } else {
      payment.getInstallments().add(installment);
    }

  }*/

  /*private void installmentPaymentForNewPayments(PaymentRequest request, Payment payment,
      Course course, double courseAmount, double requestAmount, double minimumInstallmentAmount) {
    double installmentAmount;
    if (courseAmount < requestAmount) {
      throw new IllegalArgumentException("Overpaying");
    }
    
    validateInstallmentCount(request, course);  // Extracted validation logic for installment count

    // Calculate installment amount
    if (request.getInstallmentCount() == course.getAllowedInstallment()) {
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
  }*/
  
  /*private void validateInstallmentCount(PaymentRequest request, Course course) {
    int requestInstallmentCount = request.getInstallmentCount();
    int courseInstallmentCount = course.getAllowedInstallment();
    
    if (requestInstallmentCount > courseInstallmentCount || requestInstallmentCount <= 0) {
        throw new IllegalArgumentException("Invalid installment count");
    }
  }*/
  
  private void validateInstallmentAmount(double installmentAmount, double requestAmount, double minimumInstallmentAmount) {
    if (installmentAmount > requestAmount) {
        throw new IllegalArgumentException("Not enough installment amount, you need to pay at least " + minimumInstallmentAmount);
    } else if (installmentAmount < requestAmount) {
        throw new IllegalArgumentException("Please enter the correct installment amount: " + installmentAmount);
    }
}

  /*private Payment saveExistingPayment(PaymentRequest request, Course course,
      Payment payment) {
    
    double requestAmount = request.getPaymentAmount();
    
    if(PaymentStatus.COMPLETED.equals(payment.getPaymentStatus())) {
      throw new IllegalArgumentException("Payment already completed");
    }
    
    double remaingAmount = course.getAmount() - payment.getPaidAmount();
    
    int remaiingIntallmentCount = payment.getRemainigInstallmentCount();// - payment.getInstallments().size();
    
    if(remaiingIntallmentCount == 1 ) {
      remainingInstallmentCountIsOne(payment, requestAmount, remaingAmount);
    } else if(remaiingIntallmentCount > 1) {
      remainingInstallmentCountIsMoreThanOne(request, payment, requestAmount, remaingAmount,
          remaiingIntallmentCount);
    }
    
    saveInstallment(false, payment, request.getPaymentAmount());

    return paymentRepository.save(payment);
  }*/

  /*private void remainingInstallmentCountIsOne(Payment existingPayment, double requestAmount,
      double remaingAmount) {
    if (requestAmount != remaingAmount) {
      throw new IllegalArgumentException(requestAmount < remaingAmount ?
          "Not enough payment amount" : "Overcharging");
    }

    existingPayment.setPaidAmount(existingPayment.getPaidAmount() + remaingAmount);
    existingPayment.setPaymentStatus(PaymentStatus.COMPLETED);
    existingPayment.setRemainigInstallmentCount(0);
  }*/

  /*private void remainingInstallmentCountIsMoreThanOne(PaymentRequest request,
      Payment existingPayment, double requestAmount, double remaingAmount,
      int remaiingIntallmentCount) {
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
  }*/

}
