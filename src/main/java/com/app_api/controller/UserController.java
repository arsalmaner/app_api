package com.app_api.controller;

import com.app_api.dto.request.ChangePasswordRequest;
import com.app_api.dto.request.UserRequest;
import com.app_api.service.UserService;
import com.app_api.auth.AuthenticationService;
import com.app_api.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final AuthenticationService authenticationService;

    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request) {
        service.changePassword(request);
        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('whser')")
    public ResponseEntity<?> changeActive(
            @PathVariable Integer id,
            @RequestParam boolean status) {
        service.changeActive(id, status);
        return ResponseEntity.accepted().build();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> updateUser(
            @RequestBody UserRequest userRequest) {
        service.updateUser(userRequest);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<?> userDetail() {
        return ResponseEntity.ok(service.getUserDetail());
    }

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('whser')")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }
}
