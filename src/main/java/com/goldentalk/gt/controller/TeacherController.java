package com.goldentalk.gt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.goldentalk.gt.dto.ErrorResponseDto;
import com.goldentalk.gt.dto.TeacherRequestDto;
import com.goldentalk.gt.dto.TeacherResponseDto;
import com.goldentalk.gt.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "CRUD REST APIs for Teacher",
    description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE teacher details"
)
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

  private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);
  
  private TeacherService teacherService;

  public TeacherController(TeacherService teacherService) {
    super();
    this.teacherService = teacherService;
  }
  
  @Operation(
      summary = "Create Teacher REST API",
      description = "REST API to create new Teacher"
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
  @PostMapping("/create")
  @ResponseStatus(code = HttpStatus.CREATED)
  public void createTeacher(@RequestBody TeacherRequestDto request) {
    logger.info("Create teacher. ");
    teacherService.createTeacher(request);
    logger.info("create teacher done");
  }
  
  @GetMapping("/retrieve/{teacher-id}")
  public TeacherResponseDto retrieveTeacher(@PathVariable("teacher-id") String teacherId) {

    return teacherService.retrieveTeacher(teacherId);
  }
}
