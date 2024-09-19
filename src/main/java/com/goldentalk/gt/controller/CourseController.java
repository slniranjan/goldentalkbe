package com.goldentalk.gt.controller;

import com.goldentalk.gt.dto.*;
import com.goldentalk.gt.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "CRUD REST APIs for Course",
        description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE course details"
)
@RestController
@RequestMapping("/api/v1/courses")
@AllArgsConstructor
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    private CourseService courseService;

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
    @PutMapping("/teacherUpdate")
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
    @GetMapping("/{courseId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CourseResponseDto retrieveCourse(@PathVariable("courseId") Integer courseId) {

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
    public CreateCourseResponseDto createCourse(@Validated @RequestBody CreateCourseRequestDto request) {
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
    @PutMapping("/{courseId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CourseResponseDto updateCourse(@PathVariable("courseId") Integer courseId, @Validated @RequestBody CreateCourseRequestDto request) {
        return courseService.updateCourse(courseId, request);
    }

    @Operation(
            summary = "Retrieve all courses REST API",
            description = "Retrieve all courses REST API"
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
    @GetMapping()
    public List<CourseResponseDto> retrieveCourses() {
        return courseService.retriveAllCourses();
    }
}
