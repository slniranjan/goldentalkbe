package com.goldentalk.gt.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.goldentalk.gt.mapper.SectionMapper;
import org.springframework.stereotype.Service;
import com.goldentalk.gt.dto.SectionResponse;
import com.goldentalk.gt.dto.SectionResponseDTO;
import com.goldentalk.gt.entity.Section;
import com.goldentalk.gt.repository.SectionRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SectionServiceImpl implements SectionService {

    private SectionRepository sectionRepository;
    private SectionMapper mapper;

    public Optional<SectionResponseDTO> retrieveSection(Integer id) {

        Optional<Section> section = sectionRepository.findById(id);

        return getResponseDTO(section);
    }

    @Override
    public SectionResponse retrieveSectionNames() {

        Set<Section> sections = sectionRepository.findByDeleted(false);

//      section never null as data load when application load
        List<SectionResponseDTO> sectionResponses = sections.stream()
                .map(section -> mapper.toDTO(section))
                .toList();

        SectionResponse response = new SectionResponse();
        response.getSections().addAll(sectionResponses);

        return response;
    }

    @Override
    public Optional<SectionResponseDTO> getSectionByName(String name) {

        Optional<Section> section = sectionRepository.findBySectionNameIgnoreCase(name);

        return getResponseDTO(section);
    }


    private Optional<SectionResponseDTO> getResponseDTO(Optional<Section> section) {
        if (section.isPresent()) {
            SectionResponseDTO sectionResponseDTO = mapper.toDTO(section.get());
            return Optional.of(sectionResponseDTO);
        }

        return Optional.empty();
    }

}
