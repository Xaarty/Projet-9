package com.medilabo.front.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InMemoryUserDetailsManager userDetailsManager;

    @Test
    void shouldEncodePassword() {
        assertTrue(passwordEncoder.matches("password", passwordEncoder.encode("password")));
    }

    @Test
    void shouldLoadDoctorUser() {
        assertTrue(userDetailsManager.userExists("doctor"));
    }
}