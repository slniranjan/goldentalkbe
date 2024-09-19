package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.TeacherRequestDto;
import com.goldentalk.gt.dto.TeacherResponseDto;

import java.util.List;

public interface TeacherService {

  void createTeacher(TeacherRequestDto request);
  
  TeacherResponseDto retrieveTeacher(Integer teacherId);

  List<TeacherResponseDto> retrieveTeachers();

}
