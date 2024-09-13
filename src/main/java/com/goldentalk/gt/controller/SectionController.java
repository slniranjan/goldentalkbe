package com.goldentalk.gt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.goldentalk.gt.dto.RetrieveSectionResponse;
import com.goldentalk.gt.dto.SectionResponse;
import com.goldentalk.gt.dto.SectionResponseDTO;
import com.goldentalk.gt.service.SectionService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/section")
@AllArgsConstructor
public class SectionController {

  private SectionService sectionService;

  @GetMapping("/{section-id}")
  public RetrieveSectionResponse retrieveSections(@PathVariable("section-id") String sectionId) {


    return sectionService.retrieveSection();
  }
  
  @GetMapping
  public SectionResponse retrieveSectionNames() {
    
    return sectionService.retrieveSectionNames();
    
  }


}
