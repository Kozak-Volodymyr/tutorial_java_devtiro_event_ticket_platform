package com.eventSystem.events.config;

import com.eventSystem.events.filters.UserProvisioningFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserProvisioningFilter filter, JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        http
                .authorizeHttpRequests(authorize->
                        authorize.requestMatchers(HttpMethod.GET,"/api/v1/published-events/**").permitAll()
                                .requestMatchers("/api/v1/events").hasRole("ORGANIZER")
                                .requestMatchers(HttpMethod.POST,"/api/v1/ticket-validations").hasRole("STAFF")
                                .anyRequest().authenticated()

                ).csrf(csrf->csrf.disable())
                .sessionManagement(session->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2ResourceServer(oauth2->
                        oauth2.jwt(jwt->jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
                .addFilterAfter(filter, BearerTokenAuthenticationFilter.class);
        return http.build();
    }
}
