package com.sparky.service;

import com.sparky.model.Company;
import com.sparky.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompanyServiceTest {

	@Mock
	private CompanyRepository companyRepository;

	@InjectMocks
	private CompanyService companyService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetCompanyById() {
		Company company = new Company();
		company.setId(1L);
		company.setName("Test Company");

		when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

		Company result = companyService.getCompanyById(1L);

		assertNotNull(result);
		assertEquals("Test Company", result.getName());
	}
}
