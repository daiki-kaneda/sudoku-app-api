package com.example.sudoku_app_api.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "spring.security.oauth2.resourceserver.jwt")
public class JwtProperties {
    private String issuerUri;
    private String expectedAudience;
}
