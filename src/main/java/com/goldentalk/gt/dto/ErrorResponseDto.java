package com.goldentalk.gt.dto;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
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
  private HttpStatus errorCode;
  
  @Schema(
      description =  "Error Message"
  )
  private String errorMessage;
  
  @Schema(
      description =  "Time when the error happend"
  )
  private LocalDateTime errorTime;
}