package com.goldentalk.gt.entity;

import java.util.Set;
import com.goldentalk.gt.entity.enums.PaymentStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
  
  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;
  
  private double installmentAmount;
  
  private double minimumInstallmentAmount;
  
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
