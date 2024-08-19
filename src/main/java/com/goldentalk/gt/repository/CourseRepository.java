package com.goldentalk.gt.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.repository.CrudRepository;
import com.goldentalk.gt.entity.Course;

public interface CourseRepository extends CrudRepository<Course, Integer> {

  Set<Course> findByIdInAndIsDeleted(List<Integer> id, boolean isDeleted);
}
