package com.goldentalk.gt.service;

import java.util.Set;
import org.springframework.stereotype.Service;
import com.goldentalk.gt.dto.TeacherRequestDto;
import com.goldentalk.gt.dto.TeacherResponseDto;
import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Qualification;
import com.goldentalk.gt.entity.Section;
import com.goldentalk.gt.entity.Teacher;
import com.goldentalk.gt.exception.CourseNotFoundException;
import com.goldentalk.gt.exception.SectionNotFoundException;
import com.goldentalk.gt.repository.CourseRepository;
import com.goldentalk.gt.repository.SectionRepository;
import com.goldentalk.gt.repository.TeacherRepository;

@Service
public class TeacherServiceImpl implements TeacherService {
  
  private SectionRepository sectionRepository;
  
  private CourseRepository courseRepository;
  
  private TeacherRepository teacherRepository;
  
  public TeacherServiceImpl(SectionRepository sectionRepository,
      CourseRepository courseRepository,
      TeacherRepository teacherRepository) {
    super();
    this.sectionRepository = sectionRepository;
    this.courseRepository = courseRepository;
    this.teacherRepository = teacherRepository;
  }

  @Override
  public void createTeacher(TeacherRequestDto request) {
    
    Teacher teacher = new Teacher();
    teacher.setName(request.getName());
    teacher.setNic(request.getNic());
    teacher.setPhoneNumber(request.getPhoneNumber());
    
    Section section = sectionRepository
        .findById(request.getSectionId())
        .orElseThrow(() ->  new SectionNotFoundException("Section Not Found for the given id " + request.getSectionId()));
    
    teacher.setSection(section);
    
    Set<Course> courses = courseRepository.findByIdInAndIsDeleted(request.getCourseIds(), false);
    
    if(courses.isEmpty()) {
      throw new CourseNotFoundException("No Course found for the given Ids");
    }
    
    teacher.setCourses(courses);
    
    request.getQualifications().forEach(q -> {
      Qualification qa = new Qualification();
      qa.setQualification(q.getQualification());
      qa.setInstitute(q.getInstitute());
      qa.setTeacher(teacher);
      qa.setDeleted(false);
      teacher.getQualifications().add(qa);
      qa.setTeacher(teacher);
    });
    
    teacher.setDeleted(false);
    
    Teacher savedTeacher = teacherRepository.save(teacher);
    
    courses.forEach(c -> {
      c.setTeacher(savedTeacher);
      courseRepository.save(c);
    });
    
  }

  @Override
  public TeacherResponseDto retrieveTeacher(String teacherId) {
    Teacher teacher = teacherRepository.findByTeacherId(teacherId);
    TeacherResponseDto response = new TeacherResponseDto();
    response.setName(teacher.getName());
    response.setTeacherId(teacher.getTeacherId());
    
    teacher.getCourses().forEach(c -> {
      response.getCourseNames().add(c.getName());
    });
    
    response.setSection(teacher.getSection().getSectionName());
    
    return response;
  }

}
