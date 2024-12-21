package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.TeacherRequestDto;
import com.goldentalk.gt.dto.TeacherResponseDto;
import com.goldentalk.gt.entity.Teacher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TeacherService {

    @Transactional
    Integer createTeacher(TeacherRequestDto request);

    TeacherResponseDto retrieveTeacher(Integer teacherId);

    List<TeacherResponseDto> retrieveTeachers();

    TeacherResponseDto updateTeacher(Integer id, TeacherRequestDto request);

    List<TeacherResponseDto> retrieveTeachersInSection(Integer sectionId);
}
