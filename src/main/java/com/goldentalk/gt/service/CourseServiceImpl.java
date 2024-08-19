package com.goldentalk.gt.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.goldentalk.gt.dto.CourseResponseDto;
import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.repository.CourseRepository;

@Service
public class CourseServiceImpl implements CourseService {
  
  private CourseRepository courseRepository;
  
  public CourseServiceImpl(CourseRepository courseRepository) {
    super();
    this.courseRepository = courseRepository;
  }

  @Override
  public CourseResponseDto retrieveCourse(String courseId) {
    // TODO Auto-generated method stub
    
    
    return null;
  }

  
}
