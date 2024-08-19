package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.TeacherRequestDto;
import com.goldentalk.gt.dto.TeacherResponseDto;

public interface TeacherService {

  void createTeacher(TeacherRequestDto request);
  
  TeacherResponseDto retrieveTeacher(String teacherId);
  
}
