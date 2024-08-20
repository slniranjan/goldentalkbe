package com.goldentalk.gt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCourseToTeacherRequestDto {

  private String teacherId;
  
  private String courseId;
  
}
