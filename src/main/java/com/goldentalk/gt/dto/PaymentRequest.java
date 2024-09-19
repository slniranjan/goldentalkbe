package com.goldentalk.gt.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PaymentRequest {

  private String studentId;
  
  private Integer id;
  
  private double paymentAmount;
  
  private boolean installment;
  
  private int installmentCount;
  
}
