package com.goldentalk.gt.repository;

import org.springframework.data.repository.CrudRepository;
import com.goldentalk.gt.entity.Student;

public interface StudentRepository extends CrudRepository<Student, Integer> {

}
