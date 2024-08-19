package com.goldentalk.gt.repository;

import org.springframework.data.repository.CrudRepository;
import com.goldentalk.gt.entity.Teacher;

public interface TeacherRepository extends CrudRepository<Teacher, Integer> {

  Teacher findByTeacherId(String teacherId);
}
