package com.goldentalk.gt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.goldentalk.gt.dto.CreateAndUpdateStudentRequest;
import com.goldentalk.gt.dto.CreateAndUpdateStudentResponse;
import com.goldentalk.gt.dto.StudentResponseDto;
import com.goldentalk.gt.service.StudentService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/student")
@AllArgsConstructor
public class StudentController {
  
  private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

  private StudentService studentService;
  
  @PostMapping
  @ResponseStatus(value = HttpStatus.CREATED)
  public CreateAndUpdateStudentResponse createStudent(@RequestBody CreateAndUpdateStudentRequest request) {
    logger.info("Creating a student");
    
    return studentService.createStudent(request);
  }
  
  @GetMapping("/{student-id}")
  public StudentResponseDto retrieveStrudent(@PathVariable("student-id") String studentId) {
    return studentService.retrieveStudents(studentId);
  }
  
  @PutMapping("/{student-id}")
  public CreateAndUpdateStudentResponse updateStudent(@PathVariable("student-id") String studentId, @RequestBody CreateAndUpdateStudentRequest request) {
    
    return studentService.updateStudent(studentId, request);
  }
  
  @DeleteMapping("/{student-id}")
  public boolean deleteStudent(@PathVariable("student-id") String studentId) {
    return studentService.deleteStudent(studentId);
  }
}
