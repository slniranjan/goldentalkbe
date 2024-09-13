package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.RetrieveSectionResponse;
import com.goldentalk.gt.dto.SectionResponse;

public interface SectionService {

  RetrieveSectionResponse retrieveSection();
  
  SectionResponse retrieveSectionNames();
  
}
