package org.dbp.hackaton.hackaton1.domain;

import lombok.RequiredArgsConstructor;
import org.dbp.hackaton.hackaton1.dto.CompanyConsumptionDTO;
import org.dbp.hackaton.hackaton1.dto.CompanyDTO;
import org.dbp.hackaton.hackaton1.dto.CreateCompanyRequest;
import org.dbp.hackaton.hackaton1.dto.UpdateCompanyRequest;
import org.dbp.hackaton.hackaton1.repository.CompanyRepository;
import org.dbp.hackaton.hackaton1.repository.UserRepository;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyDTO createCompany(CreateCompanyRequest request) {
        Company company = new Company();
        company.setName(request.getName());
        company.setRuc(request.getRuc());
        company.setSubscriptionDate(LocalDate.now());
        company.setActive(true);

        User admin = new User();
        admin.setName(request.getAdminName());
        admin.setEmail(request.getAdminEmail());
        admin.setPassword(request.getAdminPassword());
        admin.setRole(Role.ROLE_COMPANY_ADMIN);
        admin.setCompany(company);

        company.setAdmin(admin);

        companyRepository.save(company);
        userRepository.save(admin);

        return mapToDTO(company);
    }

    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CompanyDTO getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));
        return mapToDTO(company);
    }

    public CompanyDTO updateCompany(Long id, UpdateCompanyRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));

        company.setName(request.getName());
        company.setRuc(request.getRuc());

        return mapToDTO(companyRepository.save(company));
    }

    public void toggleCompanyStatus(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));

        company.setActive(!company.isActive());
        companyRepository.save(company);
    }

    public CompanyConsumptionDTO getConsumptionReport(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));

        CompanyConsumptionDTO dto = new CompanyConsumptionDTO();
        dto.setCompanyId(company.getId());
        dto.setCompanyName(company.getName());
        dto.setTotalRequests(123);
        dto.setTotalTokens(4567);
        dto.setRequestsPerModel(Map.of(
                "OPENAI", 100,
                "META", 23
        ));

        return dto;
    }

    private CompanyDTO mapToDTO(Company company) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setRuc(company.getRuc());
        dto.setSubscriptionDate(company.getSubscriptionDate());
        dto.setActive(company.isActive());
        dto.setAdminEmail(company.getAdmin().getEmail());
        return dto;
    }
}
