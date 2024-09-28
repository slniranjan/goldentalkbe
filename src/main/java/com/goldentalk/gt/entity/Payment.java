package com.goldentalk.gt.entity;

import com.goldentalk.gt.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
//@Data
//@EqualsAndHashCode(callSuper = false)
public class Payment extends BaseEntity {

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

    @ManyToOne(fetch = FetchType.LAZY) // Prevents loading the Student when retrieving Payment
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    private Course course;

    private boolean deleted;
}
