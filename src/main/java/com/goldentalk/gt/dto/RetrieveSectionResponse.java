package com.goldentalk.gt.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetrieveSectionResponse {

  private String setionId;
  
  private String sectionName;
  
  private Set<StudentResponseDto> students;
  
  private Set<CourseResponseDto> courses;
  
  private Set<TeacherResponseDto> teachers;
  
}
