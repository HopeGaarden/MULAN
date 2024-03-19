package com.example.back.config;

import com.example.back.domain.auth.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class SecurityConfigTest extends IntegrationHelper {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("인증되지 않은 사용자는 허용되지 않은 엔드포인트에 접근할 수 없다.")
    void unAuthUserTest() throws Exception {// given
        String uri = "/test";

        // when
        mockMvc.perform(
                        get(uri)
                )
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("모든 사용자는 인증 없이 \"/auth/**\" URI에 접근 가능하다.")
    void authUriPermitAllTest() throws Exception {
        // given
        String expectBody = "test!!";
        String uri = "/auth/test";

        // when
        mockMvc.perform(
                        get(uri)
                )
                // then
                .andExpect(status().isOk())
                .andExpect(content().string(expectBody))
                .andDo(print());
    }
}