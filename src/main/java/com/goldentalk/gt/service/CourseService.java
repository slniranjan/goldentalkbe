package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.CourseResponseDto;

public interface CourseService {

  CourseResponseDto retrieveCourse(String courseId);
}
