package com.goldentalk.gt.dto;

import java.util.HashSet;
import java.util.Set;
import com.goldentalk.gt.entity.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDetailsDTO {

  private int paymentId;
  
  private PaymentStatus paymentStatus;
  
  private String courseId;
  
  private double paidAmount;
  
  private Set<InstallmentDTO> installments = new HashSet<InstallmentDTO>();;
}
