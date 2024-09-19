package com.goldentalk.gt.exception;

import com.goldentalk.gt.dto.ErrorResponseDto;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleSectionNotFoundExceptionHandler(NotFoundException exception, WebRequest reqeust) {
        ErrorResponseDto errorResponseDTO = ErrorResponseDto.builder()
                .apiPath(reqeust.getDescription(false))
                .errorMessage(exception.getMessage())
                .errorCode(HttpStatus.NOT_FOUND.value())
                .errorTime(LocalDateTime.now()).build();

        return new ResponseEntity<ErrorResponseDto>(errorResponseDTO, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentExceptionHandler(IllegalArgumentException exception, WebRequest reqeust) {

        ErrorResponseDto errorResponseDTO = ErrorResponseDto.builder()
                .apiPath(reqeust.getDescription(false))
                .errorMessage(exception.getMessage())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .errorTime(LocalDateTime.now()).build();

        return new ResponseEntity<ErrorResponseDto>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    // Handle @Valid and @Validated validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponseDto>> handleValidationExceptions(WebRequest request, MethodArgumentNotValidException ex) {

        List<ErrorResponseDto> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    return ErrorResponseDto.builder()
                            .apiPath(request.getDescription(false))
                            .errorMessage(((FieldError) error).getField() + " : " + ((FieldError) error).getDefaultMessage())
                            .errorCode(HttpStatus.BAD_REQUEST.value())
                            .errorTime(LocalDateTime.now())
                            .build();
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleAlreadyExistsException(AlreadyExistsException ex, WebRequest reqeust) {

        ErrorResponseDto errorResponseDTO = ErrorResponseDto.builder()
                .apiPath(reqeust.getDescription(false))
                .errorMessage(ex.getMessage())
                .errorCode(HttpStatus.CONFLICT.value())
                .errorTime(LocalDateTime.now()).build();

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.CONFLICT);
    }
}
