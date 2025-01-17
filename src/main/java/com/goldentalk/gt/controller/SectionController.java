package com.goldentalk.gt.controller;

import com.goldentalk.gt.dto.ErrorResponseDto;
import com.goldentalk.gt.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import com.goldentalk.gt.dto.RetrieveSectionResponse;
import com.goldentalk.gt.dto.SectionResponse;
import com.goldentalk.gt.dto.SectionResponseDTO;
import com.goldentalk.gt.service.SectionService;
import lombok.AllArgsConstructor;

@Tag(
        name = "CRUD REST APIs for Section",
        description = "CRUD REST APIs to get section details"
)
@RestController
@AllArgsConstructor
public class SectionController {

    public static final String SECTION_PATH = "/api/v1/sections";
    public static final String SECTION_PATH_ID = SECTION_PATH + "/{sectionId}";
    public static final String SECTION_PATH_NAME = SECTION_PATH + "/section/{sectionName}";

    private SectionService sectionService;

    @Operation(
            summary = "Get section details by section id REST API",
            description = "Get section details by section id REST API"
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
    @GetMapping(SECTION_PATH_ID)
    public SectionResponseDTO retrieveSections(@PathVariable("sectionId") Integer sectionId) {

        return sectionService.retrieveSection(sectionId)
                .orElseThrow(NotFoundException::new);
    }

    @Operation(
            summary = "Get section details by section name REST API",
            description = "Get section details by section name REST API"
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
    @GetMapping(SECTION_PATH_NAME)
    public SectionResponseDTO retrieveSectionByName(@PathVariable("sectionName") String sectionName) {

        return sectionService.getSectionByName(sectionName)
                .orElseThrow(NotFoundException::new);

    }

    @Operation(
            summary = "Get all sections REST API",
            description = "Get all sections REST API"
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
    @GetMapping(SECTION_PATH)
    public SectionResponse retrieveSectionNames() {

        return sectionService.retrieveSectionNames();

    }

}
