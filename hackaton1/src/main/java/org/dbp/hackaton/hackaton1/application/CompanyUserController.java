package org.dbp.hackaton.hackaton1.application;

import lombok.RequiredArgsConstructor;
import org.dbp.hackaton.hackaton1.domain.LimitService;
import org.dbp.hackaton.hackaton1.domain.UserService;
import org.dbp.hackaton.hackaton1.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company/users")
@RequiredArgsConstructor
public class CompanyUserController {
    private final UserService userService;
    private final LimitService limitService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(userService.getAllByCompany());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @PostMapping("/{id}/limits")
    public ResponseEntity<UserLimitDTO> assignLimit(@PathVariable Long id, @RequestBody AssignLimitRequest request) {
        return ResponseEntity.ok(limitService.assignToUser(id, request));
    }

    @GetMapping("/{id}/consumption")
    public ResponseEntity<UserConsumptionDTO> getUserConsumption(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getConsumption(id));
    }
}
