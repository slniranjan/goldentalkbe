package com.goldentalk.gt.service;

import com.goldentalk.gt.dto.SectionResponse;
import com.goldentalk.gt.dto.SectionResponseDTO;

import java.util.Optional;

public interface SectionService {

  Optional<SectionResponseDTO> retrieveSection(Integer id);
  
  SectionResponse retrieveSectionNames();

  Optional<SectionResponseDTO> getSectionByName(String name);
  
}
