package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.TeacherRequestDto;
import com.goldentalk.gt.dto.TeacherResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TeacherService {

  @Transactional
  void createTeacher(TeacherRequestDto request);
  
  TeacherResponseDto retrieveTeacher(Integer teacherId);

  List<TeacherResponseDto> retrieveTeachers();

}
