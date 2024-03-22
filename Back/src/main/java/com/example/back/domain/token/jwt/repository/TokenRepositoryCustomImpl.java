package com.example.back.domain.token.jwt.repository;

import com.example.back.domain.token.jwt.JwtToken;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.back.domain.token.jwt.QJwtToken.jwtToken;


@Component
@RequiredArgsConstructor
public class TokenRepositoryCustomImpl implements TokenRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<JwtToken> findAllValidTokenByUserId(String email) {

        return jpaQueryFactory.selectFrom(jwtToken)
                .where(jwtToken.email.eq(email))
                .fetch();
    }
}
