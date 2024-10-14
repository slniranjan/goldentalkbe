package com.goldentalk.gt.service;

import java.util.*;

import com.goldentalk.gt.exception.NotFoundException;
import com.goldentalk.gt.mapper.TeacherMapper;
import org.springframework.stereotype.Service;
import com.goldentalk.gt.dto.TeacherRequestDto;
import com.goldentalk.gt.dto.TeacherResponseDto;
import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Qualification;
import com.goldentalk.gt.entity.Section;
import com.goldentalk.gt.entity.Teacher;
import com.goldentalk.gt.repository.CourseRepository;
import com.goldentalk.gt.repository.SectionRepository;
import com.goldentalk.gt.repository.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private SectionRepository sectionRepository;
    private CourseRepository courseRepository;
    private TeacherRepository teacherRepository;
    private TeacherMapper teacherMapper;

    @Override
    @Transactional
    public Integer createTeacher(TeacherRequestDto request) {

        Teacher teacher = teacherMapper.teacherRequestDtoToTeacher(request);

        Section section = sectionRepository
                .findById(request.getSectionId())
                .orElseThrow(() -> new NotFoundException("Section Not Found for the given id " + request.getSectionId()));

        teacher.setSection(section);

        Set<Course> courses = new HashSet<>();

        if (!request.getCourseIds().isEmpty()) {
            courses = courseRepository.findByIdInAndIsDeleted(request.getCourseIds(), false);
            if (courses.isEmpty())
                throw new NotFoundException("Course not found for the given course ids " + request.getCourseIds());
            teacher.setCourses(courses);
        } else {
            teacher.setCourses(null);
        }

        teacher.getQualifications().forEach(qualification -> qualification.setTeacher(teacher));

        Teacher savedTeacher = teacherRepository.save(teacher);

        if (!request.getCourseIds().isEmpty()) {
            if (!courses.isEmpty()) {
                courses.forEach(c -> {
                    c.setTeacher(savedTeacher);
                    courseRepository.save(c);
                });
            }
        }

        return savedTeacher.getId();
    }

    @Override
    public TeacherResponseDto retrieveTeacher(Integer teacherId) {

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("Teacher not found for the given id " + teacherId));

        return teacherMapper.teacherToTeacherResponseDto(teacher);
    }

    @Override
    public List<TeacherResponseDto> retrieveTeachers() {

//        Iterable<Teacher> teachers = teacherRepository.findAll();
//        List<TeacherResponseDto> dto = new ArrayList<>();
//
//        teachers.forEach(teacher -> {
//            dto.add(teacherMapper.toDto(teacher));
//        });
//
//        return dto;
        return null;
    }

    @Override
    public void updateTeacher(Integer id, TeacherRequestDto request) {
        Section section = sectionRepository
                .findById(request.getSectionId())
                .orElseThrow(() -> new NotFoundException("Section Not Found for the given id " + request.getSectionId()));

        teacherRepository.findById(id)
                .map(existingTeacher -> {
                    existingTeacher.setName(request.getName());
                    existingTeacher.setNic(request.getNic());
                    existingTeacher.setPhoneNumber(request.getPhoneNumber());
                    existingTeacher.setSection(section);
                    existingTeacher.setCourses(courseRepository.findByIdInAndIsDeleted(request.getCourseIds(), false));
                    return teacherRepository.save(existingTeacher);
                })
                .orElseThrow(() -> new NotFoundException("Teacher not found for the given id " + id));
    }

}
