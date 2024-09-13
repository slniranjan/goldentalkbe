package com.goldentalk.gt.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.goldentalk.gt.dto.RetrieveSectionResponse;
import com.goldentalk.gt.dto.SectionResponse;
import com.goldentalk.gt.dto.SectionResponseDTO;
import com.goldentalk.gt.entity.Section;
import com.goldentalk.gt.repository.SectionRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SectionServiceImpl implements SectionService {
  
  private SectionRepository sectionRepository;
  
  @Override
  public RetrieveSectionResponse retrieveSection() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SectionResponse retrieveSectionNames() {
    
    Set<Section> sections = sectionRepository.findByDeleted(false);
    
    List<SectionResponseDTO> sectionResponses = sections.stream()
        .map(section -> new SectionResponseDTO(section.getId(), section.getSectionName()))
        .collect(Collectors.toList());
    
    SectionResponse response = new SectionResponse();
    response.getSections().addAll(sectionResponses);
    
    return response;
  }

}
