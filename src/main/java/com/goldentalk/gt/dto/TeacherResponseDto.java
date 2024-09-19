package com.goldentalk.gt.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class TeacherResponseDto {

  private String name;
  
  private Integer id;
  
  private String section;
  
  private List<String> courseNames;
  
}
