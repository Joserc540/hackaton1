package org.dbp.hackaton.hackaton1.domain;

import lombok.RequiredArgsConstructor;
import org.dbp.hackaton.hackaton1.config.exception.ResourceNotFoundException;
import org.dbp.hackaton.hackaton1.dto.CreateUserRequest;
import org.dbp.hackaton.hackaton1.dto.UpdateUserRequest;
import org.dbp.hackaton.hackaton1.dto.UserConsumptionDTO;
import org.dbp.hackaton.hackaton1.dto.UserDTO;
import org.dbp.hackaton.hackaton1.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public UserDTO create(CreateUserRequest request) {
        User admin = authService.getAuthenticatedUser();
        Company company = admin.getCompany();

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setCompany(company);

        return mapToDTO(userRepository.save(user));
    }

    public List<UserDTO> getAllByCompany() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.ROLE_USER)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return mapToDTO(user);
    }

    public UserDTO update(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        return mapToDTO(userRepository.save(user));
    }

    public UserConsumptionDTO getConsumption(Long userId) {
        UserConsumptionDTO dto = new UserConsumptionDTO();
        dto.setUserId(userId);
        dto.setTotalRequests(30);
        dto.setTotalTokens(1234);
        dto.setRequestsPerModel(Map.of("OPENAI", 20, "META", 10));
        return dto;
    }

    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}
