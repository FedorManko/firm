package com.myproject.firm.config.security;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthConverter jwtAuthConverter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(request -> request
            .requestMatchers("/", "/swagger-ui/**", "/api-docs/**", "/actuator/**")
            .permitAll()
            .requestMatchers(POST, "/auth/**").permitAll()
            .requestMatchers(GET, "/employee/**").permitAll()
            .anyRequest().authenticated());

    http.oauth2ResourceServer(
        auth -> auth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));

    http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));

    return http.build();
  }

}
