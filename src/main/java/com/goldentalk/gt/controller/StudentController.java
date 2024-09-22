package com.goldentalk.gt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/students")
@AllArgsConstructor
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private StudentService studentService;

    @PostMapping
    public ResponseEntity<CreateAndUpdateStudentResponse> createStudent(@Validated @RequestBody CreateAndUpdateStudentRequest request) {
        logger.info("Creating a student");

        CreateAndUpdateStudentResponse student = studentService.createStudent(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(student.getId())
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>(student, headers, HttpStatus.CREATED);
    }

    @GetMapping("/{studentId}")
    public StudentResponseDto retrieveStrudent(@PathVariable String studentId) {
        return studentService.retrieveStudents(studentId);
    }

    @PutMapping("/{studentId}")
    public CreateAndUpdateStudentResponse updateStudent(@PathVariable String studentId, @Validated @RequestBody CreateAndUpdateStudentRequest request) {

        return studentService.updateStudent(studentId, request);
    }

    @DeleteMapping("/{student-id}")
    public boolean deleteStudent(@PathVariable("student-id") String studentId) {
        return studentService.deleteStudent(studentId);
    }
}
