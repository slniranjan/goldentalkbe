package com.goldentalk.gt.repository;

import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    Set<Course> findByIdInAndIsDeleted(Set<Integer> id, boolean isDeleted);

    @Query("SELECT c FROM Course c JOIN FETCH c.students s WHERE s.deleted = false AND c.id = :id AND c.isDeleted = :isDeleted")
    Optional<Course> findActiveCourseById(Integer id, boolean isDeleted);

    @Query("SELECT c FROM Course c JOIN FETCH c.students s WHERE s.deleted = false AND c.isDeleted = :isDeleted")
    List<Course> findAllActiveCourses(boolean isDeleted);

    Optional<Course> findByNameAndIsDeleted(String name, boolean isDeleted);

    Optional<Course> findByIdAndSectionAndIsDeleted(Integer id, Section section, boolean isDeleted);

    @Modifying
    @Transactional
    @Query("UPDATE Course c SET c.category = :category, c.name = :name, c.installment = :isInstallment, c.fee = :fee WHERE c.id = :id AND c.isDeleted = false")
    int updateCourse(@Param("id") Integer id,
                     @Param("category") String category,
                     @Param("name") String name,
                     @Param("isInstallment") Boolean isInstallment,
                     @Param("fee") Double fee);

}
