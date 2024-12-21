package com.goldentalk.gt.repository;

import com.goldentalk.gt.entity.Payment;
import com.goldentalk.gt.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Payment p SET p.secondPaymentAmount = :payment, p.paymentStatus = :status WHERE p.id = :id")
    int updateSecondPaymentAmount(@Param("id") Integer id, @Param("payment") Double payment, @Param("status") PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE p.nextPaymentDate BETWEEN :startDate AND :endDate AND p.paymentStatus = :status AND p.deleted = :deleted")
    List<Payment> findUpcomingPayments(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("status") PaymentStatus status, @Param("deleted") Boolean deleted);

    List<Payment> findByNextPaymentDateBeforeAndDeleted(LocalDateTime currentDate, Boolean deleted);

}
