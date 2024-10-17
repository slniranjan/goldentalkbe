package com.goldentalk.gt.controller;

import com.goldentalk.gt.dto.CourseResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
import lombok.AllArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(
        name = "CRUD REST APIs for Teacher",
        description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE teacher details"
)
@RestController
@RequestMapping("/api/v1/teachers")
@AllArgsConstructor
public class TeacherController {

    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    private TeacherService teacherService;

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
    @PostMapping()
    public ResponseEntity<String> createTeacher(@Validated @RequestBody TeacherRequestDto request) {
        logger.info("Create teacher. ");
        Integer id = teacherService.createTeacher(request);
        logger.info("create teacher done");

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest() // Current request URI (e.g., /api/users)
                .path("/{id}")        // Append the ID path
                .buildAndExpand(id) // Replace {id} with the actual ID
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>("", headers, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Teacher REST API",
            description = "REST API to Get Teacher by teacherid"
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
    @GetMapping("/{teacherId}")
    public ResponseEntity<TeacherResponseDto> retrieveTeacherById(@PathVariable Integer teacherId) {

        return new ResponseEntity<>(teacherService.retrieveTeacher(teacherId), HttpStatus.OK);
    }

    @Operation(
            summary = "Get All Teachers REST API",
            description = "REST API to Get All Teachers"
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
    public ResponseEntity<List<TeacherResponseDto>> retrieveTeachers() {

        return new ResponseEntity<>(teacherService.retrieveTeachers(), HttpStatus.OK);
    }

    @Operation(
            summary = "Update teacher REST API",
            description = "REST API to Update teacher details"
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
    @PutMapping("/{teacherId}")
    public ResponseEntity<TeacherResponseDto> updateCourseForTheTeacher(@PathVariable Integer teacherId,
                                                            @Validated @RequestBody TeacherRequestDto request) {

        return ResponseEntity.ok(teacherService.updateTeacher(teacherId, request));


        /*teacherService.updateTeacher(teacherId, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest() // Current request URI (e.g., /api/users)
                .path("/{id}")        // Append the ID path
                .buildAndExpand(teacherId) // Replace {id} with the actual ID
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);*/

//        return new ResponseEntity<>("", null, HttpStatus.CONFLICT);


    }

}
