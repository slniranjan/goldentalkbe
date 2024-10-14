package com.goldentalk.gt.repository;

import com.goldentalk.gt.entity.Payment;
import com.goldentalk.gt.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Payment p SET p.secondPaymentAmount = :payment, p.paymentStatus = :status WHERE p.id = :id")
    int updateSecondPaymentAmount(@Param("id") Integer id, @Param("payment") Double payment, @Param("status") PaymentStatus status);

}
