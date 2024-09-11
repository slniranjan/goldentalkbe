package com.goldentalk.gt.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDetailsDTO {

  private int paymentId;
  
  private String paymentStatus;
  
  private String courseId;
  
  private Set<InstallmentDTO> installments = new HashSet<InstallmentDTO>();;
}
