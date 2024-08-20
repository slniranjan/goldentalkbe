package com.goldentalk.gt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.goldentalk.gt.dto.AddCourseToTeacherRequestDto;
import com.goldentalk.gt.dto.CourseResponseDto;
import com.goldentalk.gt.dto.CreateCourseRequestDto;
import com.goldentalk.gt.dto.CreateCourseResponseDto;
import com.goldentalk.gt.dto.ErrorResponseDto;
import com.goldentalk.gt.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "CRUD REST APIs for Course",
    description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE course details"
)
@RestController
@RequestMapping("/api/course")
public class CourseController {
  
  private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

  private CourseService courseService;

  public CourseController(CourseService courseService) {
    super();
    this.courseService = courseService;
  }
  
  @Operation(
      summary = "Add teacher to Course REST API",
      description = "REST API to Add teacher to Course"
  )
  @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "HTTP Status CREATED"
        ),
        @ApiResponse(
                responseCode = "400",
                description = "HTTP Status bad request",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                )
        )
  }
  )
  @PutMapping("/teacher-update")
  @ResponseStatus(value = HttpStatus.CREATED)
  public CourseResponseDto updateCourseForTheTeacher(@RequestBody AddCourseToTeacherRequestDto request) {
    
    return courseService.addCourseToTeacher(request);
    
  }
  
  @Operation(
      summary = "Get course details by course id REST API",
      description = "Get course details by course id REST API"
  )
  @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "HTTP Status OK"
        ),
        @ApiResponse(
                responseCode = "400",
                description = "HTTP Status bad request",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                )
        )
  }
  )
  @GetMapping("/{course-id}")
  @ResponseStatus(value = HttpStatus.OK)
  public CourseResponseDto retrieveCourse(@PathVariable("course-id") String courseId) {
    
    return courseService.retrieveCourse(courseId);
  }
  
  @Operation(
      summary = "Create course REST API",
      description = "Create course REST API"
  )
  @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "HTTP Status OK"
        ),
        @ApiResponse(
                responseCode = "400",
                description = "HTTP Status bad request",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                )
        )
  }
  )
  @PostMapping
  @ResponseStatus(value = HttpStatus.CREATED)
  public CreateCourseResponseDto createCourse(@RequestBody CreateCourseRequestDto request) {
    return courseService.createCourse(request);
  }
  
  @Operation(
      summary = "Update course REST API",
      description = "Update course REST API"
  )
  @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "HTTP Status OK"
        ),
        @ApiResponse(
                responseCode = "400",
                description = "HTTP Status bad request",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                )
        )
  }
  )
  @PutMapping("/{course-id}")
  @ResponseStatus(value = HttpStatus.CREATED)
  public CourseResponseDto updateCourse(@PathVariable("course-id") String courseId, @RequestBody CreateCourseRequestDto request) {
    return courseService.updateCourse(courseId, request);
  }
}
