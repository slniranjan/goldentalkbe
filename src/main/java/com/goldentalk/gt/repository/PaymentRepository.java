package com.goldentalk.gt.repository;

import java.util.List;
import java.util.Set;

import com.goldentalk.gt.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Payment;
import com.goldentalk.gt.entity.Student;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

//  Payment findByCourseIdAndStudentId(int courseId, int studentId);

    @Modifying
    @Query("UPDATE Payment p SET p.secondPaymentAmount = :payment WHERE p.student.id = :studentId AND p.course.id = :courseId")
    int updateSecondPaymentAmount(@Param("studentId") Integer studentId, @Param("courseId") Integer courseId, @Param("payment") Double payment);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Payment p SET p.secondPaymentAmount = :payment, p.paymentStatus = :status WHERE p.id = :id")
    int updateSecondPaymentAmount(@Param("id") Integer id, @Param("payment") Double payment, @Param("status") PaymentStatus status);

}
