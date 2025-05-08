package org.dbp.hackaton.hackaton1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dbp.hackaton.hackaton1.application.CompanyAdminController;
import org.dbp.hackaton.hackaton1.domain.CompanyService;
import org.dbp.hackaton.hackaton1.dto.CompanyConsumptionDTO;
import org.dbp.hackaton.hackaton1.dto.CompanyDTO;
import org.dbp.hackaton.hackaton1.dto.CreateCompanyRequest;
import org.dbp.hackaton.hackaton1.dto.UpdateCompanyRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompanyAdminController.class)
class CompanyAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompanyService companyService;

    @Test
    void testCreateCompany() throws Exception {
        CreateCompanyRequest request = new CreateCompanyRequest();
        request.setName("Company A");
        request.setRuc("123456789");
        request.setAdminName("Admin A");
        request.setAdminEmail("admin@example.com");
        request.setAdminPassword("password123");

        CompanyDTO responseDto = new CompanyDTO();
        responseDto.setId(1L);
        responseDto.setName("Company A");
        responseDto.setRuc("123456789");
        responseDto.setSubscriptionDate(LocalDate.of(2025, 5, 8));
        responseDto.setActive(true);
        responseDto.setAdminEmail("admin@example.com");

        when(companyService.createCompany(any(CreateCompanyRequest.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/admin/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Company A"))
                .andExpect(jsonPath("$.ruc").value("123456789"))
                .andExpect(jsonPath("$.subscriptionDate").value("2025-05-08"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.adminEmail").value("admin@example.com"));
    }

    @Test
    void testGetAllCompanies() throws Exception {
        CompanyDTO companyDto = new CompanyDTO();
        companyDto.setId(1L);
        companyDto.setName("Company A");
        companyDto.setRuc("123456789");
        companyDto.setSubscriptionDate(LocalDate.of(2025, 5, 8));
        companyDto.setActive(true);
        companyDto.setAdminEmail("admin@example.com");

        when(companyService.getAllCompanies()).thenReturn(List.of(companyDto));

        mockMvc.perform(get("/api/admin/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Company A"))
                .andExpect(jsonPath("$[0].ruc").value("123456789"))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    void testGetCompany() throws Exception {
        long companyId = 1L;
        CompanyDTO companyDto = new CompanyDTO();
        companyDto.setId(companyId);
        companyDto.setName("Company A");
        companyDto.setRuc("123456789");
        companyDto.setSubscriptionDate(LocalDate.of(2025, 5, 8));
        companyDto.setActive(false);
        companyDto.setAdminEmail("admin@example.com");

        when(companyService.getCompanyById(companyId)).thenReturn(companyDto);

        mockMvc.perform(get("/api/admin/companies/{id}", companyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(companyId))
                .andExpect(jsonPath("$.name").value("Company A"))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void testUpdateCompany() throws Exception {
        long companyId = 1L;
        UpdateCompanyRequest request = new UpdateCompanyRequest();
        request.setName("Company Updated");
        request.setRuc("987654321");

        CompanyDTO responseDto = new CompanyDTO();
        responseDto.setId(companyId);
        responseDto.setName("Company Updated");
        responseDto.setRuc("987654321");
        responseDto.setSubscriptionDate(LocalDate.of(2025, 5, 8));
        responseDto.setActive(true);
        responseDto.setAdminEmail("admin@example.com");

        when(companyService.updateCompany(eq(companyId), any(UpdateCompanyRequest.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/admin/companies/{id}", companyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(companyId))
                .andExpect(jsonPath("$.name").value("Company Updated"))
                .andExpect(jsonPath("$.ruc").value("987654321"));
    }

    @Test
    void testToggleStatus() throws Exception {
        long companyId = 1L;
        doNothing().when(companyService).toggleCompanyStatus(companyId);

        mockMvc.perform(patch("/api/admin/companies/{id}/status", companyId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetConsumption() throws Exception {
        long companyId = 1L;
        CompanyConsumptionDTO consumptionDto = new CompanyConsumptionDTO();
        consumptionDto.setCompanyId(companyId);
        consumptionDto.setCompanyName("Company A");
        consumptionDto.setTotalRequests(100);
        consumptionDto.setTotalTokens(5000);
        consumptionDto.setRequestsPerModel(Map.of("OPENAI", 60, "META", 40));

        when(companyService.getConsumptionReport(companyId)).thenReturn(consumptionDto);

        mockMvc.perform(get("/api/admin/companies/{id}/consumption", companyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyId").value(companyId))
                .andExpect(jsonPath("$.companyName").value("Company A"))
                .andExpect(jsonPath("$.totalRequests").value(100))
                .andExpect(jsonPath("$.totalTokens").value(5000))
                .andExpect(jsonPath("$.requestsPerModel.OPENAI").value(60));
    }
}
