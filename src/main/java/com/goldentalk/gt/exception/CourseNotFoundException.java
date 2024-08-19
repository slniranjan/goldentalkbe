package com.goldentalk.gt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CourseNotFoundException extends RuntimeException {

  public CourseNotFoundException(String message) {
    super(message);
  }

  
}
