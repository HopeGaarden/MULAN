package com.example.back.config.security.filter;

import com.example.back.api.auth.service.jwt.JwtTokenProvider;
import com.example.back.global.exception.ExceptionMessage;
import com.example.back.global.exception.TokenException;
import com.example.back.common.response.JsonResult;
import com.example.back.common.utils.AuthenticationExtractor;
import com.example.back.domain.token.jwt.repository.JwtTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;
    private final JwtTokenRepository jwtTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("[BR INFO]: Jwt 인증 필터에 진입합니다");

        // 헤더에서 JWT 토큰 추출
        final Optional<String> extractToken = AuthenticationExtractor.extractToken(request);
        final String jwtToken;
        final String subject;

        // JWT 토큰이 BEARER 형식이 아니거나 존재하지 않는다면 다음 필터로
        if (extractToken.isEmpty()) {
            log.info("[BR INFO]: Jwt 토큰이 헤더에 없으므로 다음 필터로 이동합니다");

            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwtToken = extractToken.get();

            // 식별자 추출
            subject = jwtTokenProvider.extractSubject(jwtToken);

            // subject이 null인 경우 예외 발생
            if (subject == null) {
                log.error("[BR ERROR]: {}", ExceptionMessage.JWT_SUBJECT_IS_NULL.getText());
                throw new TokenException(ExceptionMessage.JWT_SUBJECT_IS_NULL);
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(subject);

            // 활성화 상태의 토큰인지 검증
            boolean isTokenValid = jwtTokenRepository.findById(jwtToken)
                    .map(token -> !token.isRevoked())
                    .orElse(false);

            // 토큰 유효성 검증
            if (jwtTokenProvider.isTokenValid(jwtToken, userDetails) && isTokenValid) {

                // UserDetails를 사용해 Authentication 생성
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                // Authentication에 현재 요청 정보를 저장
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));

                // Security Context에 Authentication 등록
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);

                log.info("[BR INFO]: Jwt 토큰이 성공적으로 인증되었습니다");

                // JWT 토큰 인증을 마치면 다음 인증 필터로 이동
                filterChain.doFilter(request, response);

            } else {
                jwtExceptionHandler(response, ExceptionMessage.JWT_TOKEN_INVALID);
            }

        } catch (ExpiredJwtException e) {
            jwtExceptionHandler(response, ExceptionMessage.JWT_TOKEN_EXPIRED);

        } catch (UnsupportedJwtException e) {
            jwtExceptionHandler(response, ExceptionMessage.JWT_UNSUPPORTED);

        } catch (MalformedJwtException e) {
            jwtExceptionHandler(response, ExceptionMessage.JWT_MALFORMED);

        } catch (SignatureException e) {
            jwtExceptionHandler(response, ExceptionMessage.JWT_SIGNATURE);

        } catch (IllegalArgumentException e) {
            jwtExceptionHandler(response, ExceptionMessage.JWT_ILLEGAL_ARGUMENT);
        }
    }

    // 모든 JWT Exception을 처리하는 핸들러
    private void jwtExceptionHandler(HttpServletResponse response, ExceptionMessage message) throws IOException {
        log.error("[BR ERROR]: {}", message.getText());

        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(JsonResult.failOf(message.getText())));
    }
}
