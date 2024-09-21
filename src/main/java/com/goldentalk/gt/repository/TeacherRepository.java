package com.goldentalk.gt.repository;

import com.goldentalk.gt.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Optional<Teacher> findByIdAndIsDeleted(Integer teacherId, boolean isDeleted);

}
