package com.example.back.config.security;

import com.example.back.api.auth.service.LogoutService;
import com.example.back.config.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LogoutService logoutService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequest ->
                        authorizeHttpRequest
                                // UnAuth Area
                                .requestMatchers("/auth/**").permitAll()
                                // WebSock Area
                                .requestMatchers("/ws/**").permitAll()
                                // Chat Area - 테스트를 위해 잠시 열어둠
                                .requestMatchers("/chat").permitAll()
                                // Others
                                .anyRequest().authenticated()
                )
                .sessionManagement((sessionManagement) ->
                        sessionManagement
                                // JWT 토큰 기반의 인증을 사용하기 위해 무상태 세션 정책 사용
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logoutConfig -> {
                    logoutConfig
                            .logoutUrl("/auth/logout")
                            .addLogoutHandler(logoutService)
                            .logoutSuccessHandler((request, response, authentication)
                                    -> SecurityContextHolder.clearContext()
                            );
                });

        return http.build();
    }
}