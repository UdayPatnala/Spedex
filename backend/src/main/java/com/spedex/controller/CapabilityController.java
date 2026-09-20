package com.spedex.controller;

import com.spedex.dto.UserCapabilitiesDto;
import com.spedex.model.User;
import com.spedex.repository.UserRepository;
import com.spedex.service.UserCapabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/capabilities")
public class CapabilityController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCapabilityService capabilityService;

    @GetMapping
    public ResponseEntity<UserCapabilitiesDto> getCapabilities() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(capabilityService.getCapabilities(user));
    }
}
