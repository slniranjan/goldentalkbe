package com.goldentalk.gt.service;

import java.util.List;

import com.goldentalk.gt.dto.CourseResponseDto;
import com.goldentalk.gt.dto.CreateCourseRequestDto;
import com.goldentalk.gt.dto.CreateCourseResponseDto;
import com.goldentalk.gt.dto.UpdateCourseRequestDto;

public interface CourseService {

    CourseResponseDto retrieveCourse(Integer courseId);

    List<CourseResponseDto> retriveAllCourses();

    CourseResponseDto addCourseToTeacher(Integer courseId, Integer teacherId);

    CreateCourseResponseDto createCourse(CreateCourseRequestDto request);

    CourseResponseDto updateCourse(Integer id, UpdateCourseRequestDto request);

    List<CourseResponseDto> retrieveAllCoursesInSection(Integer sectionId);
}
