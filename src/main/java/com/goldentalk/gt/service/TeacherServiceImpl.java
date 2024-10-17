package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.TeacherRequestDto;
import com.goldentalk.gt.dto.TeacherResponseDto;
import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Qualification;
import com.goldentalk.gt.entity.Section;
import com.goldentalk.gt.entity.Teacher;
import com.goldentalk.gt.exception.NotFoundException;
import com.goldentalk.gt.mapper.TeacherMapper;
import com.goldentalk.gt.repository.CourseRepository;
import com.goldentalk.gt.repository.SectionRepository;
import com.goldentalk.gt.repository.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

        Section section = sectionRepository.findById(request.getSectionId()).orElseThrow(() -> new NotFoundException("Section Not Found for the given id " + request.getSectionId()));

        teacher.setSection(section);

        Set<Course> courses = new HashSet<>();

        if (!request.getCourseIds().isEmpty()) {
            courses = courseRepository.findByIdInAndIsDeleted(request.getCourseIds(), false);
            if (courses.isEmpty())
                throw new NotFoundException("Course not found for the given course ids " + request.getCourseIds());

            Optional<Course> teacherExistCourse = courses.stream().filter(course -> course.getTeacher() != null).findFirst();

            if (teacherExistCourse.isPresent())
                throw new IllegalArgumentException("Course " + teacherExistCourse.get().getName() + " has already been assigned to the teacher " + teacherExistCourse.get().getTeacher().getName());

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

        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new NotFoundException("Teacher not found for the given id " + teacherId));

        return teacherMapper.teacherToTeacherResponseDto(teacher);
    }

    @Override
    public List<TeacherResponseDto> retrieveTeachers() {

        return teacherRepository.findAll().stream().map(teacher -> teacherMapper.teacherToTeacherResponseDto(teacher)).toList();
    }

    /**
     * There is no way to change teacher between sections.
     * can't use this method to update courses for the teacher.
     *
     * @param id
     * @param request
     * @return
     */
    @Override
    @Transactional
    public TeacherResponseDto updateTeacher(Integer id, TeacherRequestDto request) {

        Teacher updatedTeacher = teacherRepository.findById(id).map(existingTeacher -> {
            existingTeacher.setName(request.getName());
            existingTeacher.setNic(request.getNic());
            existingTeacher.setPhoneNumber(request.getPhoneNumber());

            Set<Qualification> existingTeacherQualifications = existingTeacher.getQualifications();
            Set<Qualification> newQualifications = teacherMapper.qualificationDtoToQualification(request.getQualifications());

            for (Qualification q : newQualifications) {
                Optional<Qualification> first = existingTeacherQualifications.stream().filter(existing -> q.getQualification().equalsIgnoreCase(existing.getQualification()) && q.getInstitute().equalsIgnoreCase(existing.getInstitute())).findFirst();

                if (first.isEmpty()) {
                    existingTeacherQualifications.add(q);
                }
            }

            existingTeacherQualifications.forEach(qualification -> qualification.setTeacher(existingTeacher));
            existingTeacher.setQualifications(existingTeacherQualifications);

            return teacherRepository.save(existingTeacher);
        }).orElseThrow(() -> new NotFoundException("Teacher not found for the given id " + id));

        return teacherMapper.teacherToTeacherResponseDto(updatedTeacher);
    }

}
