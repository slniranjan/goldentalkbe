package com.goldentalk.gt.service;

import java.util.List;
import java.util.stream.Collectors;

import com.goldentalk.gt.exception.*;
import org.springframework.stereotype.Service;
import com.goldentalk.gt.dto.CourseResponseDto;
import com.goldentalk.gt.dto.CreateCourseRequestDto;
import com.goldentalk.gt.dto.CreateCourseResponseDto;
import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Section;
import com.goldentalk.gt.entity.Teacher;
import com.goldentalk.gt.repository.CourseRepository;
import com.goldentalk.gt.repository.SectionRepository;
import com.goldentalk.gt.repository.TeacherRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private CourseRepository courseRepository;

    private TeacherRepository teacherRepository;

    private SectionRepository sectionRepository;

    @Override
    public CourseResponseDto retrieveCourse(Integer id) {

        Course course = courseRepository.findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new NotFoundException("Course Not found for the id " + id));

        return transformCourseToResponse(course);
    }

    @Override
    public CourseResponseDto addCourseToTeacher(Integer courseId, Integer teacherId) {

        Course course = courseRepository.findByIdAndIsDeleted(courseId, false)
                .orElseThrow(() -> new NotFoundException("Course Not found for the id " + courseId));

        Teacher teacher = teacherRepository.findByIdAndIsDeleted(teacherId, false)
                .orElseThrow(() -> new NotFoundException("Teacher not found for the id " + teacherId));

        course.setTeacher(teacher);
        Course persistedCourse = courseRepository.save(course);

        return transformCourseToResponse(persistedCourse);
    }

    private CourseResponseDto transformCourseToResponse(Course course) {

        Integer teacherId = 0;
        String name = "";
        if (course.getTeacher() != null) {
            teacherId = course.getTeacher().getId();
            name = course.getTeacher().getName();
        }

        List<String> studentIds = course.getStudents().stream().map(st -> st.getStudentId()).collect(Collectors.toList());

//        response.setStudentIds(studentIds);
//        response.setStudentCount(studentIds.size());

        return CourseResponseDto.builder()
                .id(course.getId())
                .category(course.getCategory())
                .courseName(course.getName())
                .courseFee(course.getFee())
                .isInstallment(course.isInstallment())
                .teacherId(teacherId)
                .teacherName(name)
                .studentIds(studentIds)
                .studentCount(studentIds.size())
                .build();


//        CourseResponseDto response = new CourseResponseDto();
////        response.setCourseId(course.getCourseId());
//        response.setCourseName(course.getName());
//        response.setCourseFee(course.getFee());
//        response.setInstallment(course.isInstallment());
//        response.setNumOfInstallments(course.getAllowedInstallment());


    }

    @Override
    public CreateCourseResponseDto createCourse(CreateCourseRequestDto request) {

        if (courseRepository.findByNameAndIsDeleted(request.getName(), false).isPresent())
            throw new AlreadyExistsException("Course already existing with the name " + request.getName());

        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new NotFoundException("Section not found for the section id " + request.getSectionId()));

        Course persistedCourse = saveCourse(request, section);

        CreateCourseResponseDto response = new CreateCourseResponseDto();
        response.setId(persistedCourse.getId());

        return response;
    }

    private Course saveCourse(CreateCourseRequestDto request, Section section) {
        Course course = new Course();
        course.setName(request.getName());
        course.setCategory(request.getCategory());
        course.setInstallment(request.isInstallment());
        course.setFee(request.getFee());
        course.setSection(section);

        return courseRepository.save(course);
    }

    @Override
    public CourseResponseDto updateCourse(Integer id, CreateCourseRequestDto request) {
        Course course = courseRepository.findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new NotFoundException("Course not found for the id " + id));

        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new NotFoundException("Section Not found for the id " + request.getSectionId()));

        return transformCourseToResponse(updateCourse(course, request, section));
    }


    public Course updateCourse(Course course, CreateCourseRequestDto request, Section section) {

        course.setName(request.getName());
        course.setCategory(request.getCategory());
        course.setInstallment(request.isInstallment());
//        course.setAllowedInstallment(request.getAllowedInstallment());
//        course.setAmount(request.getAmount());
        course.setSection(section);

        return courseRepository.save(course);
    }

    @Override
    public List<CourseResponseDto> retriveAllCourses() {

        List<Course> course = (List<Course>) courseRepository.findAll();

        return course.stream().map(c -> {
            CourseResponseDto cr = transformCourseToResponse(c);
            return cr;
        }).collect(Collectors.toList());

    }
}
