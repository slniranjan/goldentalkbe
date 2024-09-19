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
@Setter @Getter
public class Installment {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  
  private double paymentAmount;
  
  private LocalDateTime paymentDate;
  
  @ManyToOne
  private Payment payment;
  
  private boolean deleted;
  
}
