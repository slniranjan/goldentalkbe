package com.goldentalk.gt.repository;

import com.goldentalk.gt.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    Optional<Student> findByStudentIdAndDeleted(String studentId, boolean deleted);

}
