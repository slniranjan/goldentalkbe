package com.goldentalk.gt.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import com.goldentalk.gt.entity.Section;

public interface SectionRepository extends JpaRepository<Section, Integer> {

  Optional<Section> findByIdAndIsDeleted(Integer sectionId, boolean deleted);
//  Set<Section> findByIdInAndDeleted(List<Integer> sectionId, boolean deleted);

  Set<Section> findByDeleted(boolean deleted);

  Optional<Section> findBySectionNameIgnoreCase(String sectionName);

}
