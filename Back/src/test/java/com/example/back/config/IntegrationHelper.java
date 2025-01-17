package com.example.back.config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void init() {
        // 현재 실행된 포트 지정
        RestAssured.port = this.port;

        // 테스트 환경인 H2 데이터베이스인지 확인
        validateH2Database();

        // 외래키 제약 조건 해제
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE"); // H2의 모든 외래 키 제약 조건 해제


        // public 테이블을 조회한 후 'TRUNCATE TABLE (TABLE_NAME);' 형식의 쿼리로 생성
        List<String> truncateAllTablesQuery = jdbcTemplate.queryForList(
                "SELECT CONCAT('TRUNCATE TABLE ', TABLE_NAME, ';') AS q " +
                        "FROM INFORMATION_SCHEMA.TABLES " +
                        "WHERE TABLE_SCHEMA = 'PUBLIC'"
                , String.class);

        // 데이터베이스의 모든 테이블 초기화
        truncateAllTables(truncateAllTablesQuery);

        // 외래키 제약 조건 재설정
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE"); // H2의 모든 외래 키 제약 조건 재설정

    }

    private void validateH2Database() {
        // H2 데이터베이스 버전 정보 가져오기 -> 실패시 예외 발생해 테스트 실패
        jdbcTemplate.queryForObject("SELECT H2VERSION() FROM DUAL", String.class);
    }

    private void truncateAllTables(List<String> truncateAllTablesQuery) {
        // truncate를 쿼리 실행
        for (String truncateQuery : truncateAllTablesQuery) {
            jdbcTemplate.execute(truncateQuery);
        }
    }
}

