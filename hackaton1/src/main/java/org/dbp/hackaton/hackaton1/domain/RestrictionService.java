package org.dbp.hackaton.hackaton1.domain;

import lombok.RequiredArgsConstructor;
import org.dbp.hackaton.hackaton1.config.exception.ResourceNotFoundException;
import org.dbp.hackaton.hackaton1.dto.CreateRestrictionRequest;
import org.dbp.hackaton.hackaton1.dto.ModelRestrictionDTO;
import org.dbp.hackaton.hackaton1.dto.UpdateRestrictionRequest;
import org.dbp.hackaton.hackaton1.repository.ModelRestrictionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestrictionService {
    private final ModelRestrictionRepository restrictionRepository;
    private final AuthService authService;

    public ModelRestrictionDTO create(CreateRestrictionRequest request) {
        Company company = authService.getAuthenticatedUser().getCompany();

        ModelRestriction restriction = new ModelRestriction();
        restriction.setModelType(request.getModelType());
        restriction.setMaxRequests(request.getMaxRequests());
        restriction.setMaxTokens(request.getMaxTokens());
        restriction.setTimeWindow(request.getTimeWindow());
        restriction.setCompany(company);

        return mapToDTO(restrictionRepository.save(restriction));
    }

    public List<ModelRestrictionDTO> getAllForCurrentCompany() {
        Company company = authService.getAuthenticatedUser().getCompany();

        return restrictionRepository.findByCompanyId(company.getId())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ModelRestrictionDTO update(Long id, UpdateRestrictionRequest request) {
        ModelRestriction restriction = restrictionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restricción no encontrada"));

        restriction.setMaxRequests(request.getMaxRequests());
        restriction.setMaxTokens(request.getMaxTokens());
        restriction.setTimeWindow(request.getTimeWindow());

        return mapToDTO(restrictionRepository.save(restriction));
    }

    public void delete(Long id) {
        if (!restrictionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Restricción no encontrada");
        }
        restrictionRepository.deleteById(id);
    }

    private ModelRestrictionDTO mapToDTO(ModelRestriction restriction) {
        ModelRestrictionDTO dto = new ModelRestrictionDTO();
        dto.setId(restriction.getId());
        dto.setModelType(restriction.getModelType());
        dto.setMaxRequests(restriction.getMaxRequests());
        dto.setMaxTokens(restriction.getMaxTokens());
        dto.setTimeWindow(restriction.getTimeWindow());
        dto.setCompanyId(restriction.getCompany().getId());
        return dto;
    }
}
