package com.goldentalk.gt.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class PaymentDetailsResponse {

  private List<PaymentDetailsDTO> paymentDetails = new ArrayList<PaymentDetailsDTO>();
  
}
