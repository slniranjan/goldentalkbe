package com.goldentalk.gt.repository;

import com.goldentalk.gt.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    Optional<Student> findByStudentIdAndDeleted(String studentId, boolean deleted);

    @Query("SELECT s FROM Student s JOIN FETCH s.courses c WHERE c.id = :courseId AND s.studentId = :studentId")
    Optional<Student> findStudentByStudentIdAndCourseId(@Param("studentId") String studentId, @Param("courseId") Integer courseId);

    @Query("SELECT s FROM Student s WHERE s.deleted = :deleted")
    List<Student> findAllActiveOrDeleted(boolean deleted);

}
