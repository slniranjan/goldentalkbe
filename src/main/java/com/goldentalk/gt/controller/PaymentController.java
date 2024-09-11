package com.goldentalk.gt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.goldentalk.gt.dto.PaymentRequest;
import com.goldentalk.gt.dto.PaymentResponse;
import com.goldentalk.gt.service.PaymentService;
import lombok.AllArgsConstructor;

@RestController("/api/payment")
@AllArgsConstructor
public class PaymentController {
  
  private PaymentService paymentService;

  @PostMapping
  @ResponseStatus(value = HttpStatus.CREATED)
  public PaymentResponse createPayment(@RequestBody PaymentRequest request ) {
    return paymentService.savePayment(request);
  }
}