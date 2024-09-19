package com.goldentalk.gt.entity;

import java.time.LocalDateTime;
import java.util.Set;
import com.goldentalk.gt.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter @Setter
public class Payment extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer paymentId;
  
  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;

  @NotNull
  private double firstPaymentAmount;

  private double secondPaymentAmount;

  @CreationTimestamp
  private LocalDateTime firstPaymentDate;

  @UpdateTimestamp
  private LocalDateTime secondPaymentDate;

  @ManyToOne
  private Student student;
  
  @ManyToOne
  private Course course;
  
  /*@OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
  private Set<Installment> installments;*/
  
  private boolean deleted;
}
