package com.goldentalk.gt.mapper;

import org.mapstruct.Mapper;
import com.goldentalk.gt.dto.CourseResponseDto;
import com.goldentalk.gt.entity.Course;

@Mapper
public interface CourseMapper {

  CourseResponseDto toDto(Course course);
  Course toEntity(CourseResponseDto dto);
  
}
