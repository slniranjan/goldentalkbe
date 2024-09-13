package com.goldentalk.gt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.goldentalk.gt.service.SectionService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/section")
@AllArgsConstructor
public class SectionController {

  private SectionService sectionService;
  
  
}
