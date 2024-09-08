package com.goldentalk.gt.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.repository.CrudRepository;
import com.goldentalk.gt.entity.Section;

public interface SectionRepository extends CrudRepository<Section, Integer>{

  Set<Section> findByIdInAndDeleted(List<Integer> sectionId, boolean deleted);
  
}
