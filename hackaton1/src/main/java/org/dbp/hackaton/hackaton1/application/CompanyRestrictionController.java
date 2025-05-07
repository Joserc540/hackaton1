package org.dbp.hackaton.hackaton1.application;

import lombok.RequiredArgsConstructor;
import org.dbp.hackaton.hackaton1.domain.RestrictionService;
import org.dbp.hackaton.hackaton1.dto.CreateRestrictionRequest;
import org.dbp.hackaton.hackaton1.dto.ModelRestrictionDTO;
import org.dbp.hackaton.hackaton1.dto.UpdateRestrictionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company/restrictions")
@RequiredArgsConstructor
public class CompanyRestrictionController {
    private final RestrictionService restrictionService;

    @PostMapping
    public ResponseEntity<ModelRestrictionDTO> create(@RequestBody CreateRestrictionRequest request) {
        return ResponseEntity.ok(restrictionService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ModelRestrictionDTO>> getAll() {
        return ResponseEntity.ok(restrictionService.getAllForCurrentCompany());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModelRestrictionDTO> update(@PathVariable Long id, @RequestBody UpdateRestrictionRequest request) {
        return ResponseEntity.ok(restrictionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restrictionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
