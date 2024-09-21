package com.goldentalk.gt.dto;

import com.goldentalk.gt.entity.Course;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class TeacherResponseDto {

  private String name;
  
  private Integer id;
  
  private String section;
  
  private List<String> courseNames;

}
