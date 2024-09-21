package com.goldentalk.gt.mapper;

import com.goldentalk.gt.dto.TeacherResponseDto;
import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Section;
import com.goldentalk.gt.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeacherMapper {

    // Custom mapping from int to String for 'age'
    @Mapping(source = "section", target = "section", qualifiedByName = "objectToString")
    @Mapping(source = "courses", target = "courseNames", qualifiedByName = "setToList")
    TeacherResponseDto toDto(Teacher teacher);

//    Teacher toEntity(TeacherResponseDto dto);

    // Custom method to convert Section object to String
    @Named("objectToString")
    default String objectToString(Section section) {
        return section.getSectionName();
    }

    @Named("setToList")
    default List<String> extracted(Set<Course> set) {
        ArrayList<String> names = new ArrayList<>();
        set.forEach(c -> names.add(c.getName()));

        return names;
    }
}
