package com.goldentalk.gt.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.goldentalk.gt.dto.ErrorResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(SectionNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleSectionNotFoundExceptionHandler(SectionNotFoundException exception, WebRequest reqeust) {
    ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
        reqeust.getDescription(false),
        HttpStatus.BAD_REQUEST,
        exception.getMessage(),
        LocalDateTime.now()
     );
    
    return new ResponseEntity<ErrorResponseDto>(errorResponseDTO, HttpStatus.BAD_REQUEST);
  }
}
