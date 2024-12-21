package com.goldentalk.gt.dto;

import com.goldentalk.gt.entity.Student;
import com.goldentalk.gt.entity.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationDto {

    private PaymentStatus paymentStatus;
    private Double firstPaymentAmount;
    private LocalDateTime firstPaymentDate;
    private LocalDateTime nextPaymentDate;
    private StudentResponseDto studentResponseDto;

}
