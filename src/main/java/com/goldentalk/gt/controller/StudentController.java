package com.goldentalk.gt.controller;

import com.goldentalk.gt.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.goldentalk.gt.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@AllArgsConstructor
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private StudentService studentService;

    @PostMapping
    public ResponseEntity<CreateAndUpdateStudentResponse> createStudent(@Validated @RequestBody CreateStudentRequest request) {
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

    @GetMapping("")
    public List<StudentResponseDto> getAllStudents(@Param("deleted") Boolean deleted) {
        return studentService.getAllStudents(deleted);
    }

    @GetMapping("/{studentId}")
    public StudentResponseDto retrieveStudent(@PathVariable String studentId) {
        return studentService.retrieveStudents(studentId);
    }

    @PutMapping("/{studentId}")
    public CreateAndUpdateStudentResponse updateStudent(@PathVariable String studentId, @Validated @RequestBody UpdateStudentInfoOnlyRequest request) {
        return studentService.updateStudent(studentId, request);
    }

    @PutMapping(value = "/{studentId}/courses/{courseId}", params = "payment")
    public StudentResponseDto updatePayment(@PathVariable String studentId, @PathVariable Integer courseId, @RequestParam Double payment) {
        return studentService.updateSecondPayment(studentId, courseId, payment);
    }

    @PostMapping(value = "/{studentId}/courses/{courseId}")
    public CreateAndUpdateStudentResponse addStudentToNewCourse(@PathVariable String studentId, @PathVariable Integer courseId, @Validated @RequestBody PaymentDetailsDTO request) {
        return studentService.addStudentToNewCourse(studentId, courseId, request);
    }

    @DeleteMapping("/{student-id}")
    public ResponseEntity<StudentResponseDto> deleteStudent(@PathVariable("student-id") String studentId) {
        return ResponseEntity.ok(studentService.deleteStudent(studentId));
    }

    @GetMapping("/notifications")
    @ResponseStatus(value = HttpStatus.OK)
    public List<NotificationDto> getUpcomingPayments() {
        return studentService.getUpcomingPayments();
    }

    @GetMapping("/delaying")
    @ResponseStatus(value = HttpStatus.OK)
    public List<NotificationDto> getDelayingPayments() {
        return studentService.getDelayPayments();
    }
}
