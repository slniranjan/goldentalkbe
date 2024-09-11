package com.goldentalk.gt.entity;

import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Payment extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer paymentId;
  
//  private double firstPaymentAmount;
//  
//  private LocalDateTime firstPaymentDate;
//  
//  private double secondPaymentAmount;
//  
//  private LocalDateTime secondPaymentDate;
  
  private String paymentStatus;
  
  private double installmentAmount;
  
  private double paidAmount;
  
  private int remainigInstallmentCount;
  
  @ManyToOne
  private Student student;
  
  @OneToOne
  private Course course;
  
  @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
  private Set<Installment> installments;
  
  private boolean deleted;
}
