package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.CreateAndUpdateStudentRequest;
import com.goldentalk.gt.dto.CreateAndUpdateStudentResponse;
import com.goldentalk.gt.dto.NotificationDto;
import com.goldentalk.gt.dto.StudentResponseDto;
import com.goldentalk.gt.entity.Payment;
import com.goldentalk.gt.entity.Student;

import java.util.List;

public interface StudentService {

  CreateAndUpdateStudentResponse createStudent(CreateAndUpdateStudentRequest request);
  
  StudentResponseDto retrieveStudents(String studentId);
  
  CreateAndUpdateStudentResponse updateStudent(String studentId, CreateAndUpdateStudentRequest request);

  StudentResponseDto deleteStudent(String studentId);

  StudentResponseDto updateSecondPayment(String studentId, Integer courseid, Double payment);

  List<NotificationDto> getUpcomingPayments();

  List<NotificationDto> getDelayPayments();
}
