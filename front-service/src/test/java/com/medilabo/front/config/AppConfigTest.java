package com.medilabo.front.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppConfigTest {

    @Test
    void shouldCreateRestTemplate() {
        AppConfig config = new AppConfig();

        assertNotNull(config.restTemplate());
    }
}