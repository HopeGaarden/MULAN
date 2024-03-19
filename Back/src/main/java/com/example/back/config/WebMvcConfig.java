package com.example.back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (activeProfile.equals("prod")) {
            prodProfileCorsMapping(registry);
        } else {
            devProfileCorsMapping(registry);
        }
    }
    // Cors 모두 오픈 (개발환경)
    public void devProfileCorsMapping(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    // 배포 환경
    private void prodProfileCorsMapping(CorsRegistry registry) {

    }
}
