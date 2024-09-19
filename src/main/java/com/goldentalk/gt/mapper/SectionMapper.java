package com.goldentalk.gt.mapper;

import com.goldentalk.gt.dto.SectionResponseDTO;
import com.goldentalk.gt.entity.Section;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SectionMapper {

  SectionResponseDTO toDTO(Section section);

  Section toEntity(SectionResponseDTO dto);
}
