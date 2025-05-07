package org.dbp.hackaton.hackaton1.domain;

import lombok.RequiredArgsConstructor;
import org.dbp.hackaton.hackaton1.config.exception.ResourceNotFoundException;
import org.dbp.hackaton.hackaton1.dto.AssignLimitRequest;
import org.dbp.hackaton.hackaton1.dto.UserLimitDTO;
import org.dbp.hackaton.hackaton1.repository.UserLimitRepository;
import org.dbp.hackaton.hackaton1.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LimitService {
    private final UserLimitRepository userLimitRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public UserLimitDTO assignToUser(Long userId, AssignLimitRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Company adminCompany = authService.getAuthenticatedUser().getCompany();
        if (!user.getCompany().getId().equals(adminCompany.getId())) {
            throw new SecurityException("No puedes asignar límites a usuarios de otra empresa");
        }

        UserLimit limit = new UserLimit();
        limit.setUser(user);
        limit.setModelType(request.getModelType());
        limit.setMaxRequests(request.getMaxRequests());
        limit.setMaxTokens(request.getMaxTokens());
        limit.setTimeWindow(request.getTimeWindow());

        return mapToDTO(userLimitRepository.save(limit));
    }

    private UserLimitDTO mapToDTO(UserLimit limit) {
        UserLimitDTO dto = new UserLimitDTO();
        dto.setId(limit.getId());
        dto.setUserId(limit.getUser().getId());
        dto.setModelType(limit.getModelType());
        dto.setMaxRequests(limit.getMaxRequests());
        dto.setMaxTokens(limit.getMaxTokens());
        dto.setTimeWindow(limit.getTimeWindow());
        return dto;
    }
}
