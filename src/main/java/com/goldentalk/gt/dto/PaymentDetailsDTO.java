package com.goldentalk.gt.dto;

import java.util.HashSet;
import java.util.Set;
import com.goldentalk.gt.entity.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDetailsDTO {

  private PaymentStatus paymentStatus;
  
  private String courseId;
  
  private double firstPaymentAmount;

  private double secondPaymentAmount;

}
