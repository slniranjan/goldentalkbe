package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.TeacherRequest;
import com.goldentalk.gt.dto.TeacherResponse;

public interface TeacherService {

  void createTeacher(TeacherRequest request);
  
  TeacherResponse retrieveTeacher(String teacherId);
  
}
