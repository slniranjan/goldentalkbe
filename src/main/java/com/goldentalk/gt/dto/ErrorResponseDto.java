package com.goldentalk.gt.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
@Schema(
    name="ErrorResponse",
    description = "Schema to hold error response information"
)
public class ErrorResponseDto {

  @Schema(
      description =  "API path invoked by client"
  )
  private String apiPath;
  
  @Schema(
      description =  "Error response code"
  )
  private int errorCode;
  
  @Schema(
      description =  "Error Message"
  )
  private String errorMessage;
  
  @Schema(
      description =  "Time when the error happend"
  )
  private LocalDateTime errorTime;
}
