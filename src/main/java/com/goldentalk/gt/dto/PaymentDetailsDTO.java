package com.goldentalk.gt.dto;

import java.util.HashSet;
import java.util.Set;
import com.goldentalk.gt.entity.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class PaymentDetailsDTO {

  private PaymentStatus paymentStatus;
  
  private double firstPaymentAmount;

  private double secondPaymentAmount;

}
