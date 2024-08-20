package com.goldentalk.gt.service;

import org.springframework.data.repository.CrudRepository;
import com.goldentalk.gt.entity.Student;

public interface StudentService extends CrudRepository<Student, Integer>{

}
