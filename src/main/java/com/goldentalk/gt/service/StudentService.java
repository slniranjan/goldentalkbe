package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.CreateStudentRequestDto;
import com.goldentalk.gt.dto.CreateStudentResponseDto;
import com.goldentalk.gt.dto.StudentResponseDto;

public interface StudentService {

  CreateStudentResponseDto createStudent(CreateStudentRequestDto request);
  
  StudentResponseDto retrieveStudents(String studentId);
}
