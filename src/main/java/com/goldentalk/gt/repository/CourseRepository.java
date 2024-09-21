package com.goldentalk.gt.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.repository.CrudRepository;
import com.goldentalk.gt.entity.Course;

public interface CourseRepository extends CrudRepository<Course, Integer> {

  Set<Course> findByIdInAndIsDeleted(List<Integer> id, boolean isDeleted);
  
  Optional<Course> findByIdAndIsDeleted(Integer id, boolean isDeleted);

  Optional<Course> findByNameAndIsDeleted(String name, boolean isDeleted);
  
}
