package com.goldentalk.gt.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CourseResponseDto {

  private String courseId;
  
  private String courseName;
  
  private String teacherName;
  
  private String teacherId;
  
  private List<String> studentIds;
  
  private int studnetCount;
  
  private double couseFee;
  
  private boolean isInstallment;
  
  private int numOfInstallments;
}
