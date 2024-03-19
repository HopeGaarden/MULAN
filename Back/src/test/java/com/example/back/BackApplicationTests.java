package com.example.back;

import com.example.back.config.IntegrationHelper;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // JPA Auditing 기능 활성화 - BaseEntity
class BackApplicationTests extends IntegrationHelper {

    @Test
    void contextLoads() {
    }

}
