package com.goldentalk.gt.controller;

import com.goldentalk.gt.entity.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

    @PutMapping(value = "/{studentId}/courses/{courseId}", params = "payment")
    public StudentResponseDto updatePayment(@PathVariable String studentId, @PathVariable Integer courseId, @RequestParam Double payment) {

        return studentService.updateSecondPayment(studentId, courseId, payment);
    }

    @DeleteMapping("/{student-id}")
    public boolean deleteStudent(@PathVariable("student-id") String studentId) {
        return studentService.deleteStudent(studentId);
    }
}
