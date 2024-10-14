package com.goldentalk.gt.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class CourseResponseDto {

  private Integer id;
  
  private String category;

  private String courseName;

  private String teacherName;
  
  private Integer teacherId;
  
  private List<String> studentIds;
  
  private int studentCount;
  
  private double courseFee;
  
  private Boolean installment;
  
}
