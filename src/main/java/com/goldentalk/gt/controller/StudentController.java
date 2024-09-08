package com.goldentalk.gt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.goldentalk.gt.dto.CreateStudentRequestDto;
import com.goldentalk.gt.dto.CreateStudentResponseDto;
import com.goldentalk.gt.dto.StudentResponseDto;
import com.goldentalk.gt.service.StudentService;

@RestController
@RequestMapping("/api/student")
public class StudentController {
  
  private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

  private StudentService studentService;
  
  public StudentController(StudentService studentService) {
    this.studentService = studentService;
  }
  
  @PostMapping
  @ResponseStatus(value = HttpStatus.CREATED)
  public CreateStudentResponseDto createStudent(CreateStudentRequestDto request) {
    return studentService.createStudent(request);
  }
  
  @GetMapping("/{student-id}")
  public StudentResponseDto retrieveStrudent(@PathVariable("student-id") String studentId) {
    return studentService.retrieveStudents(studentId);
  }
}
