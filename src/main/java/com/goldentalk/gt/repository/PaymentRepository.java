package com.goldentalk.gt.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.repository.CrudRepository;
import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Payment;
import com.goldentalk.gt.entity.Student;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {

//  Payment findByCourseIdAndStudentId(int courseId, int studentId);
  
  Payment findByCourseAndStudent(Course course, Student student);
  
  List<Payment> findByStudentAndCourseIn(Student student, Set<Course> course);
}
