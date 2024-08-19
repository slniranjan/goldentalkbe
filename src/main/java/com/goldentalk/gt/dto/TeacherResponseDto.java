package com.goldentalk.gt.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherResponseDto {

  private String name;
  
  private String teacherId;
  
  private String section;
  
  private List<String> courseNames = new ArrayList<String>();
  
}
