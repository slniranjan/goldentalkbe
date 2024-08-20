package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.AddCourseToTeacherRequestDto;
import com.goldentalk.gt.dto.CourseResponseDto;
import com.goldentalk.gt.dto.CreateCourseRequestDto;
import com.goldentalk.gt.dto.CreateCourseResponseDto;

public interface CourseService {

  CourseResponseDto retrieveCourse(String courseId);
  
  CourseResponseDto addCourseToTeacher(AddCourseToTeacherRequestDto request);
  
  CreateCourseResponseDto createCourse(CreateCourseRequestDto request);
  
  CourseResponseDto updateCourse(String courseId, CreateCourseRequestDto request);
}
