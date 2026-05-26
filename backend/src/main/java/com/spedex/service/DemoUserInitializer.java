package com.spedex.service;

import com.spedex.model.User;
import com.spedex.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DemoUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DemoUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        User demoUser = userRepository.findByEmail("demo@gmail.com").orElseGet(User::new);
        demoUser.setName("DEMO");
        demoUser.setEmail("demo@gmail.com");
        demoUser.setPasswordHash(passwordEncoder.encode("demogorgan"));
        demoUser.setAvatarInitials("DG");
        if (demoUser.getPlan() == null || demoUser.getPlan().isBlank()) {
            demoUser.setPlan("Premium");
        }
        userRepository.save(demoUser);
    }
}
