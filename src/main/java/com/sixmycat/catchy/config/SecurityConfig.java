package com.sixmycat.catchy.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)   // @PreAuthorize, @PostAuthorize 사용을 위해
@RequiredArgsConstructor
public class SecurityConfig {



}
