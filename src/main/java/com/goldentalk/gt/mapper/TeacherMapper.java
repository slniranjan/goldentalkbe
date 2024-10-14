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

    @Mapping(source = "section.sectionName", target = "sectionName")
    @Mapping(source = "courses", target = "courseNames", qualifiedByName = "mapCoursesName")
    @Mapping(target = "qualifications.teacher", ignore = true)
    TeacherResponseDto teacherToTeacherResponseDto(Teacher teacher);

    // Custom method to map student IDs to List<String>
    @Named("mapCoursesName")
    default List<String> mapCoursesName(Set<Course> courses) {
        return courses.stream()
                .map(course -> String.valueOf(course.getName()))
                .collect(Collectors.toList());
    }

/*    // Custom method to convert Section object to String
    @Named("objectToString")
    default String objectToString(Section section) {
        return section.getSectionName();
    }

    @Named("setToList")
    default List<String> extracted(Set<Course> set) {
        ArrayList<String> names = new ArrayList<>();
        set.forEach(c -> names.add(c.getName()));

        return names;
    }*/
}
