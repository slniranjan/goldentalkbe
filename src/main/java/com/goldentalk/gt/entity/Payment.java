package com.goldentalk.gt.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Payment extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer paymentId;
  
  private double firstPaymentAmount;
  
  private LocalDateTime firstPaymentDate;
  
  private double secondPaymentAmount;
  
  private LocalDateTime secondPaymentDate;
  
  @ManyToOne
  private Student student;
  
}
