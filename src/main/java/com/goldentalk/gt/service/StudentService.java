package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.CreateAndUpdateStudentRequest;
import com.goldentalk.gt.dto.CreateAndUpdateStudentResponse;
import com.goldentalk.gt.dto.StudentResponseDto;

public interface StudentService {

  CreateAndUpdateStudentResponse createStudent(CreateAndUpdateStudentRequest request);
  
  StudentResponseDto retrieveStudents(String studentId);
  
  CreateAndUpdateStudentResponse updateStudent(String studentId, CreateAndUpdateStudentRequest request);
  
  boolean deleteStudent(String studentId);
}
