package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.CreateAndUpdateStudentRequest;
import com.goldentalk.gt.dto.CreateAndUpdateStudentResponse;
import com.goldentalk.gt.dto.StudentResponseDto;
import com.goldentalk.gt.entity.Student;

public interface StudentService {

  CreateAndUpdateStudentResponse createStudent(CreateAndUpdateStudentRequest request);
  
  StudentResponseDto retrieveStudents(String studentId);
  
  CreateAndUpdateStudentResponse updateStudent(String studentId, CreateAndUpdateStudentRequest request);

  StudentResponseDto deleteStudent(String studentId);

  StudentResponseDto updateSecondPayment(String studentId, Integer courseid, Double payment);
}
