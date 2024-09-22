package com.goldentalk.gt.repository;

import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    Set<Course> findByIdInAndIsDeleted(List<Integer> id, boolean isDeleted);

    Optional<Course> findByIdAndIsDeleted(Integer id, boolean isDeleted);

    Optional<Course> findByNameAndIsDeleted(String name, boolean isDeleted);

    Optional<Course> findByIdAndSectionAndIsDeleted(Integer id, Section section, boolean isDeleted);

}
