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

  private Integer id;

  private String name;

  private String nic;

  private String phoneNumber;

  private SectionResponseDTO section;
//  private String sectionName;

  private Set<CourseResponseDto> courses;
//  private Set<String> courseNames;

  private Set<QualificationResponseDto> qualifications;
//  private Set<String> qualifications;

}
