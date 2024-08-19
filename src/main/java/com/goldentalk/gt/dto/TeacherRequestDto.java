package com.goldentalk.gt.dto;

import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherRequestDto {

  private String name;
  
  private String nic;
  
  private String phoneNumber;
  
  private Integer sectionId;
  
  private List<Integer> courseIds;
  
  private List<TeacherQualificationDTO> qualifications;
  
}
