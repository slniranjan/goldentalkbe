package com.goldentalk.gt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.goldentalk.gt.dto.CourseResponseDto;
import com.goldentalk.gt.service.CourseService;

@RestController
@RequestMapping("/api/course")
public class CourseController {

  private CourseService courseService;

  public CourseController(CourseService courseService) {
    super();
    this.courseService = courseService;
  }
  
  @GetMapping("/retrieve")
  public CourseResponseDto retrieveCourse(@PathVariable("course-id") String courseId) {
    
    return null;
  }
}
