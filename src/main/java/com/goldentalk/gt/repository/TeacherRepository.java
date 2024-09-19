package com.goldentalk.gt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import com.goldentalk.gt.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

  Teacher findByIdAndIsDeleted(Integer teacherId, boolean isDeleted);
  
}
