package com.goldentalk.gt.dto;

import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TeacherRequestDto {

  @NotNull
  @NotBlank
  private String name;

  @NotNull
  @NotBlank
  @Size(min = 10, max = 10)
  private String nic;

  @NotNull
  @NotBlank
  @Size(min = 10, max = 10)
  private String phoneNumber;

  @NotNull
  private Integer sectionId;

  @NotNull
  private List<Integer> courseIds;
  
  private List<TeacherQualificationDTO> qualifications;
  
}
