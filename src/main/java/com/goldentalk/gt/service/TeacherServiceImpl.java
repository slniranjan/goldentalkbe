package com.goldentalk.gt.service;

import java.util.*;

import com.goldentalk.gt.exception.NotFoundException;
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

@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private SectionRepository sectionRepository;

    private CourseRepository courseRepository;

    private TeacherRepository teacherRepository;

    @Override
    public void createTeacher(TeacherRequestDto request) {

        Teacher teacher = new Teacher();
        teacher.setName(request.getName());
        teacher.setNic(request.getNic());
        teacher.setPhoneNumber(request.getPhoneNumber());

        Section section = sectionRepository
                .findById(request.getSectionId())
                .orElseThrow(() -> new NotFoundException("Section Not Found for the given id " + request.getSectionId()));

        teacher.setSection(section);

        Set<Course> courses = courseRepository.findByIdInAndIsDeleted(request.getCourseIds(), false);

        if (courses.isEmpty()) {
//      throw new CourseNotFoundException("No Course found for the given Ids");
            teacher.setCourses(null);
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
    public TeacherResponseDto retrieveTeacher(Integer teacherId) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
//        Teacher teacher = teacherRepository.findById(teacherId);

        return TeacherResponseDto.builder()
                .name(teacher.get().getName())
//                .teacherId(teacher.getId())
                .courseNames(extracted(teacher.get().getCourses()))
                .section(teacher.get().getSection().getSectionName())
                .build();
    }

    @Override
    public List<TeacherResponseDto> retrieveTeachers() {
        Iterable<Teacher> teachers = teacherRepository.findAll();
        List<TeacherResponseDto> dto = new ArrayList<>();

        teachers.forEach(teacher -> {
            TeacherResponseDto build = TeacherResponseDto.builder()
                    .name(teacher.getName())
//                    .teacherId(teacher.getTeacherId())
                    .section(teacher.getSection().getSectionName())
                    .courseNames(extracted(teacher.getCourses()))
                    .build();
            dto.add(build);
        });

        return dto;
    }

    private static List<String> extracted(Set<Course> set) {
        ArrayList<String> names = new ArrayList<>();
        set.forEach(c -> names.add(c.getName()));

        return names;
    }

}
