package org.dbp.hackaton.hackaton1.application;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dbp.hackaton.hackaton1.domain.CompanyService;
import org.dbp.hackaton.hackaton1.dto.CompanyConsumptionDTO;
import org.dbp.hackaton.hackaton1.dto.CompanyDTO;
import org.dbp.hackaton.hackaton1.dto.CreateCompanyRequest;
import org.dbp.hackaton.hackaton1.dto.UpdateCompanyRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/companies")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_SPARKY_ADMIN')")
public class CompanyAdminController {
    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody CreateCompanyRequest request) {
        return ResponseEntity.ok(companyService.createCompany(request));
    }

    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Long id, @Valid @RequestBody UpdateCompanyRequest request) {
        return ResponseEntity.ok(companyService.updateCompany(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> toggleStatus(@PathVariable Long id) {
        companyService.toggleCompanyStatus(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/consumption")
    public ResponseEntity<CompanyConsumptionDTO> getConsumption(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getConsumptionReport(id));
    }
}
