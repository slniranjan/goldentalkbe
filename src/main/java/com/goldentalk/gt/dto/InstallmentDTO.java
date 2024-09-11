package com.goldentalk.gt.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InstallmentDTO {
  private Integer id;
  
  private double paymentAmount;
  
  private LocalDateTime paymentDate;

}
