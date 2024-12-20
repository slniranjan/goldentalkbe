package com.goldentalk.gt.entity;

import com.goldentalk.gt.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @NotNull
    private double firstPaymentAmount;

    private double secondPaymentAmount;

    private LocalDateTime firstPaymentDate;
    private LocalDateTime nextPaymentDate;


    @ManyToOne(fetch = FetchType.LAZY) // Prevents loading the Student when retrieving Payment
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    private Course course;

    private boolean deleted;

    @PrePersist
    protected void onCreate() {
        firstPaymentDate = LocalDateTime.now();
        nextPaymentDate = LocalDateTime.now().plusMonths(1);
    }
}
