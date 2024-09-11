package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.PaymentRequest;
import com.goldentalk.gt.dto.PaymentResponse;

public interface PaymentService {

  PaymentResponse savePayment(PaymentRequest request);
  
}
