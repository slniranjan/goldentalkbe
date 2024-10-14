package com.goldentalk.gt.repository;

import com.goldentalk.gt.dto.CreateCourseRequestDto;
import com.goldentalk.gt.dto.UpdateCourseRequestDto;
import com.goldentalk.gt.entity.Course;
import com.goldentalk.gt.entity.Section;
import com.goldentalk.gt.entity.enums.PaymentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    Optional<Course> findByIdAndIsDeleted(Integer id, boolean isDeleted);

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
