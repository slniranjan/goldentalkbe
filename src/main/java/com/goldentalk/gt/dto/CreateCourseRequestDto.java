package com.goldentalk.gt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCourseRequestDto {

  private String category;
  
  private String name;
  
  private boolean isIntallment;
  
  private int installmentCount;
  
  private double amount;
  
  private int sectionId;

}
