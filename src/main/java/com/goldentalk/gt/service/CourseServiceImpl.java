package com.goldentalk.gt.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.goldentalk.gt.dto.AddCourseToTeacherRequestDto;
import com.goldentalk.gt.dto.CourseResponseDto;
import com.goldentalk.gt.dto.CreateCourseRequestDto;
import com.goldentalk.gt.dto.CreateCourseResponseDto;
import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Section;
import com.goldentalk.gt.entity.Teacher;
import com.goldentalk.gt.exception.CourseAlreadyExistsException;
import com.goldentalk.gt.exception.CourseNotFoundException;
import com.goldentalk.gt.exception.SectionNotFoundException;
import com.goldentalk.gt.exception.TeacherNotFoundException;
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
  public CourseResponseDto retrieveCourse(String courseId) {
    
    Course course = courseRepository.findByCourseIdAndIsDeleted(courseId, false);
    
    if(course == null) {
      throw new CourseNotFoundException("Course Not found for the id " + courseId);
    }
    
    return transformCourseToResponse(course);
  }

  @Override
  public CourseResponseDto addCourseToTeacher(AddCourseToTeacherRequestDto request) {
    
    Course course = courseRepository.findByCourseIdAndIsDeleted(request.getCourseId(), false);
    
    if(course == null) {
      throw new CourseNotFoundException("Course Not found for the id " + request.getCourseId());
    }
    
    Teacher teacher = teacherRepository.findByTeacherIdAndIsDeleted(request.getTeacherId(), false);
    
    if(teacher == null) {
      throw new TeacherNotFoundException("Teacher not found for the id " + request.getTeacherId());
    }
    
    course.setTeacher(teacher);
    Course persistedCourse = courseRepository.save(course);

    return transformCourseToResponse(persistedCourse);
  }

  private CourseResponseDto transformCourseToResponse(Course course) {
    CourseResponseDto response = new CourseResponseDto();
    response.setCourseId(course.getCourseId());
    response.setCourseName(course.getName());
    response.setCouseFee(course.getAmount());
    response.setInstallment(course.isInstallment());
    
    if(course.getTeacher() != null) {
      response.setTeacherId(course.getTeacher().getTeacherId());
      response.setTeacherName(course.getTeacher().getName() );
    }
    List<String> studentIds = course.getStudents().stream().map(st -> st.getStudentId()).collect(Collectors.toList());
    
    response.setStudentIds(studentIds);
    response.setStudnetCount(studentIds.size());
    return response;
  }

  @Override
  public CreateCourseResponseDto createCourse(CreateCourseRequestDto request) {
    
    Course course = courseRepository.findByNameAndIsDeleted(request.getName(), false);
    
    if(course != null) {
      throw new CourseAlreadyExistsException("Course already existing with the name " + request.getName());
    }
    
    Section section = sectionRepository.findById(request.getSectionId()).orElseThrow(() -> new SectionNotFoundException("Section Not found for the id " + request.getSectionId()));
    
    Course persistedCourse = saveCourse(request, section);

    CreateCourseResponseDto response = new CreateCourseResponseDto();
    response.setCourseId(persistedCourse.getCourseId());
    
    return response;
  }
  
  public Course saveCourse(CreateCourseRequestDto request, Section section) {
    Course course = new Course();
    course.setName(request.getName());
    course.setCategory(request.getCategory());
    course.setInstallment(request.isIntallment());
    course.setInstallmentCount(request.getInstallmentCount());
    course.setAmount(request.getAmount());
    course.setSection(section);
    
    return courseRepository.save(course);
  }

  @Override
  public CourseResponseDto updateCourse(String courseId, CreateCourseRequestDto request) {
    Course course = courseRepository.findByCourseIdAndIsDeleted(courseId, false);
    
    if(course == null) {
      throw new CourseNotFoundException("Course not found for the id " + courseId);
    }
    
    Section section = sectionRepository.findById(request.getSectionId()).orElseThrow(() -> new SectionNotFoundException("Section Not found for the id " + request.getSectionId()));

    return transformCourseToResponse(updateCourse(course, request, section));
  }
  
  
  public Course updateCourse(Course course, CreateCourseRequestDto request, Section section) {

    course.setName(request.getName());
    course.setCategory(request.getCategory());
    course.setInstallment(request.isIntallment());
    course.setInstallmentCount(request.getInstallmentCount());
    course.setAmount(request.getAmount());
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
