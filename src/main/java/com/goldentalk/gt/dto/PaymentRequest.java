package com.goldentalk.gt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

  private String studentId;
  
  private String courseId;
  
  private double paymentAmount;
  
  private boolean installment;
  
  private int installmentCount;
  
}
