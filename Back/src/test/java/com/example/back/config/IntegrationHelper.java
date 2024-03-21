package com.example.back.config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.util.List;

/*
    랜덤 포트를 사용해 내장 서버로 테스트 환경 실행
    - 여러 번 실행할 때 포트 충돌을 방지
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationHelper extends AbstractTestExecutionListener {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String NON_ASCII = "NonAsciiCharacters";

    // 무작위로 선택한 포트를 주입
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void init() {
        // 현재 실행된 포트 지정
        RestAssured.port = this.port;

        // 테스트 환경인 H2 데이터베이스인지 확인
        validateMySQLDatabase();

        // 데이터베이스의 모든 테이블 초기화
        truncateAllTables();
    }

    private void validateMySQLDatabase() {
        // MySQL 버전 정보 가져오기 -> 실패시 예외 발생해 테스트 실패
        jdbcTemplate.queryForObject("SELECT VERSION()", String.class);
    }

    private void truncateAllTables() {
        // 테이블 목록 가져오기
        List<String> tableNames = jdbcTemplate.queryForList(
                "SELECT table_name FROM information_schema.tables " +
                        "WHERE table_schema = DATABASE()", String.class);

        // 테이블 초기화
        for (String tableName : tableNames) {
            jdbcTemplate.execute("TRUNCATE TABLE " + tableName);
        }
    }
}

