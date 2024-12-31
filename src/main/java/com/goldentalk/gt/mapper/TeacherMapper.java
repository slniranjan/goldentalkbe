package com.goldentalk.gt.mapper;

import com.goldentalk.gt.dto.TeacherQualificationDTO;
import com.goldentalk.gt.dto.TeacherRequestDto;
import com.goldentalk.gt.dto.TeacherResponseDto;
import com.goldentalk.gt.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeacherMapper {

    @Mapping(target = "section", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Teacher teacherRequestDtoToTeacher(TeacherRequestDto teacherRequestDto);

    @Mapping(target = "teacher", ignore = true)
    Set<Qualification> qualificationDtoToQualification(Set<TeacherQualificationDTO> teacherQualificationDtos);

//    @Mapping(source = "section.sectionName", target = "sectionName")
//    @Mapping(source = "section", target = "section")
//    @Mapping(source = "courses", target = "courseNames", qualifiedByName = "mapCoursesName")
//    @Mapping(source = "qualifications", target = "qualifications", qualifiedByName = "mapQualificationsName")
    TeacherResponseDto teacherToTeacherResponseDto(Teacher teacher);

    @Mapping(source = "section.sectionName", target = "sectionName")
    @Mapping(source = "courses", target = "courseNames", qualifiedByName = "mapCoursesName")
    @Mapping(source = "qualifications", target = "qualifications", qualifiedByName = "mapQualificationsName")
    List<TeacherResponseDto> teacherToTeacherResponseDto(List<Teacher> teacher);

    // Custom method to map student IDs to List<String>
    @Named("mapCoursesName")
    default Set<String> mapCoursesName(Set<Course> courses) {
        return courses.stream()
                .map(course -> String.valueOf(course.getName()))
                .collect(Collectors.toSet());
    }

    @Named("mapQualificationsName")
    default Set<String> mapQualificationsName(Set<Qualification> qualifications) {
        return qualifications.stream()
                .map(qualification -> String.valueOf(qualification.getQualification()))
                .collect(Collectors.toSet());
    }
}
