package com.goldentalk.gt.mapper;

import com.goldentalk.gt.dto.CourseResponseDto;
import com.goldentalk.gt.dto.CreateCourseRequestDto;
import com.goldentalk.gt.dto.CreateCourseResponseDto;
import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {

    @Mapping(source = "name", target = "courseName")
    @Mapping(source = "fee", target = "courseFee")
    @Mapping(source = "teacher.name", target = "teacherName")
    @Mapping(source = "teacher.id", target = "teacherId")
    @Mapping(source = "students", target = "studentIds", qualifiedByName = "mapStudentIds")
    @Mapping(target = "studentCount", expression = "java(course.getStudents().size())")
    CourseResponseDto courseToCourseResponseDto(Course course);

    @Mapping(target = "section", ignore = true)
    Course createCourseRequestDtoToCourse(CreateCourseRequestDto dto);

    CreateCourseResponseDto courseToCreateCourseResponseDto(Course course);

    @Mapping(source = "name", target = "courseName")
    @Mapping(source = "fee", target = "courseFee")
    @Mapping(source = "teacher.name", target = "teacherName")
    @Mapping(source = "teacher.id", target = "teacherId")
    @Mapping(source = "students", target = "studentIds", qualifiedByName = "mapStudentIds")
    @Mapping(target = "studentCount", expression = "java(course.getStudents().size())")
    List<CourseResponseDto> courseToCourseResponseDto(List<Course> courses);

    // Custom method to map student IDs to List<String>
    @Named("mapStudentIds")
    default List<String> mapStudentIds(Set<Student> students) {
        return students.stream()
                .map(student -> String.valueOf(student.getStudentId()))
                .collect(Collectors.toList());
    }

}
