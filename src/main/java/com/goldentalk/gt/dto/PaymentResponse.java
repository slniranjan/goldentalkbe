package com.goldentalk.gt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResponse {

  private String paymentId;
  
  private double amount;
  
}