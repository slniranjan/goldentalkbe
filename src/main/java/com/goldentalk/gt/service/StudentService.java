package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.*;

import java.util.List;

public interface StudentService {

  CreateAndUpdateStudentResponse createStudent(CreateStudentRequest request);
  
  StudentResponseDto retrieveStudents(String studentId);
  
  CreateAndUpdateStudentResponse updateStudent(String studentId, UpdateStudentInfoOnlyRequest request);

  StudentResponseDto deleteStudent(String studentId);

  StudentResponseDto updateSecondPayment(String studentId, Integer courseid, Double payment);

  List<NotificationDto> getUpcomingPayments();

  List<NotificationDto> getDelayPayments();

  List<StudentResponseDto> getAllStudents(Boolean deleted);

  CreateAndUpdateStudentResponse addStudentToNewCourse(String studentId, Integer courseId, PaymentDetailsDTO request);
}
