package com.goldentalk.gt.dto;

import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Qualification;
import com.goldentalk.gt.entity.Section;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class TeacherResponseDto {

  private String name;
  
  private Integer id;
  
  private String sectionName;
  
  private List<String> courseNames;

  private Set<Qualification> qualifications;

}
