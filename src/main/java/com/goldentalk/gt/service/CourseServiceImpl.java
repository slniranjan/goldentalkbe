package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.CourseResponseDto;
import com.goldentalk.gt.dto.CreateCourseRequestDto;
import com.goldentalk.gt.dto.CreateCourseResponseDto;
import com.goldentalk.gt.dto.UpdateCourseRequestDto;
import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Section;
import com.goldentalk.gt.entity.Teacher;
import com.goldentalk.gt.exception.AlreadyExistsException;
import com.goldentalk.gt.exception.NotFoundException;
import com.goldentalk.gt.mapper.CourseMapper;
import com.goldentalk.gt.repository.CourseRepository;
import com.goldentalk.gt.repository.SectionRepository;
import com.goldentalk.gt.repository.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private CourseRepository courseRepository;
    private TeacherRepository teacherRepository;
    private SectionRepository sectionRepository;
    private CourseMapper courseMapper;

    @Override
    public CourseResponseDto retrieveCourse(Integer id) {

        Course course = courseRepository.findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new NotFoundException("Course Not found for the id " + id));

        return courseMapper.courseToCourseResponseDto(course);

    }

    @Override
    public CourseResponseDto addCourseToTeacher(Integer courseId, Integer teacherId) {

        Course course = courseRepository.findByIdAndIsDeleted(courseId, false)
                .orElseThrow(() -> new NotFoundException("Course Not found for the id " + courseId));

        Teacher teacher = teacherRepository.findByIdAndIsDeleted(teacherId, false)
                .orElseThrow(() -> new NotFoundException("Teacher not found for the id " + teacherId));

        course.setTeacher(teacher);
        Course persistedCourse = courseRepository.save(course);

        return courseMapper.courseToCourseResponseDto(persistedCourse);
    }

    @Override
    public CreateCourseResponseDto createCourse(CreateCourseRequestDto request) {

        if (courseRepository.findByNameAndIsDeleted(request.getName(), false).isPresent())
            throw new AlreadyExistsException("Course already existing with the name " + request.getName());

        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new NotFoundException("Section not found for the section id " + request.getSectionId()));

        Course course = courseMapper.createCourseRequestDtoToCourse(request);
        course.setSection(section);

        Course persistedCourse = courseRepository.save(course);

        return courseMapper.courseToCreateCourseResponseDto(persistedCourse);
    }

    @Override
    @Transactional
    public CourseResponseDto updateCourse(Integer id, UpdateCourseRequestDto request) {
        int status = courseRepository.updateCourse(id, request.getCategory(), request.getName(), request.getInstallment(), request.getFee());

        if (status == 0)
            throw new NotFoundException("Course not found for the id " + id);

        Optional<Course> course = courseRepository.findByIdAndIsDeleted(id, false);

        return courseMapper.courseToCourseResponseDto(course.get());

    }

    @Override
    public List<CourseResponseDto> retriveAllCourses() {
        List<Course> courseList = courseRepository.findAll();

        return courseMapper.courseToCourseResponseDto(courseList);
    }
}
