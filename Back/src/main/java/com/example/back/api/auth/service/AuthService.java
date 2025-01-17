package com.example.back.api.auth.service;

import com.example.back.alarm.domain.auth.RegisteredEvent;
import com.example.back.api.auth.controller.request.AuthRequest;
import com.example.back.api.auth.controller.request.SignUpRequest;
import com.example.back.api.auth.service.jwt.JwtTokenProvider;
import com.example.back.api.auth.service.response.AuthResponse;
import com.example.back.domain.auth.disease.DiseaseInfo;
import com.example.back.domain.auth.disease.DiseaseMember;
import com.example.back.domain.auth.disease.DiseaseMemberStatus;
import com.example.back.domain.auth.disease.repository.DiseaseInfoRepository;
import com.example.back.domain.auth.disease.repository.DiseaseMemberRepository;
import com.example.back.domain.auth.medical.MedicalInfo;
import com.example.back.domain.auth.medical.constant.VerifyStatus;
import com.example.back.domain.auth.medical.repository.MedicalInfoRepository;
import com.example.back.domain.auth.member.constant.MemberRole;
import com.example.back.global.event.Events;
import com.example.back.global.exception.*;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.auth.member.repository.MemberRepository;
import com.example.back.domain.token.jwt.JwtToken;
import com.example.back.domain.token.jwt.repository.JwtTokenRepository;
import com.example.back.domain.token.refresh.RefreshToken;
import com.example.back.domain.token.refresh.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private static final String ROLE_CLAIM = "role";
    private static final String NICKNAME_CLAIM = "nickname";

    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final MedicalInfoRepository medicalInfoRepository;
    private final OCRService ocrService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenRepository jwtTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final DiseaseInfoRepository diseaseInfoRepository;
    private final DiseaseMemberRepository diseaseMemberRepository;

    // 로그인 로직
    @Transactional
    public AuthResponse authenticate(AuthRequest login) {
        Member member = memberRepository.findByEmail(login.email()).orElseThrow(() -> {
            log.error("[BR ERROR]: {}", ExceptionMessage.MEMBER_NOT_FOUND.getText());
            throw new MemberException(ExceptionMessage.MEMBER_NOT_FOUND);
        });

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                login.email(),
                login.password()
        ));

        log.info("[BR INFO]: {} 남이 로그인에 성공하셨습니다.", member.getNickname());

        // 이전의 모든 JWT 토큰 사용 불가능하도록 업데이트
        revokeAllUserTokens(member);

        // JWT 토큰 생성
        String jwtToken = generateAndSaveJwtToken(member);

        // refresh token 생성
        String refreshToken = generateAndSaveRefreshToken(member);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokeAllUserTokens(Member member) {
        // email에 해당하는 토큰 중 활성화되어 있는 토큰 리스트 조회
        List<JwtToken> validTokens = jwtTokenRepository.findByEmail(member.getEmail()).stream()
                .filter(token -> !token.isRevoked())
                .toList();

        if (!validTokens.isEmpty()) {
            validTokens.forEach(JwtToken::setTokenInvalid);
            jwtTokenRepository.saveAll(validTokens);
        }
    }

    private static HashMap<String, String> setClaims(Member member) {
        HashMap<String, String> claims = new HashMap<>();
        claims.put(ROLE_CLAIM, member.getRole().name());
        claims.put(NICKNAME_CLAIM, member.getNickname());
        return claims;
    }

    private String generateAndSaveJwtToken(Member member) {
        String jwtToken = jwtTokenProvider.generateToken(setClaims(member), member);

        JwtToken token = JwtToken.builder()
                .token(jwtToken)
                .revoked(false)
                .email(member.getEmail())
                .build();

        jwtTokenRepository.save(token);
        return jwtToken;
    }

    private String generateAndSaveRefreshToken(Member member) {
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);

        RefreshToken token = RefreshToken.builder()
                .refreshToken(refreshToken)
                .email(member.getEmail())
                .isRevoked(false)
                .build();

        refreshTokenRepository.save(token);
        return refreshToken;
    }

    // 토큰 재발급 로직
    @Transactional
    public AuthResponse refreshJwtToken(String refreshToken) {
        if (refreshToken == null) {
            log.error("[BR ERROR]: {}", ExceptionMessage.REFRESH_TOKEN_IS_NULL.getText());
            throw new TokenException(ExceptionMessage.REFRESH_TOKEN_IS_NULL);
        }

        final String userEmail = jwtTokenProvider.extractSubject(refreshToken);

        if (userEmail == null) {
            log.error("[BR ERROR]: {}", ExceptionMessage.JWT_SUBJECT_IS_NULL.getText());
            throw new TokenException(ExceptionMessage.JWT_SUBJECT_IS_NULL);
        }

        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> {
            log.error("[BR ERROR]: {}", ExceptionMessage.MEMBER_NOT_FOUND.getText());
            throw new MemberException(ExceptionMessage.MEMBER_NOT_FOUND);
        });

        // 유효한 refreshToken인지 확인
        if (jwtTokenProvider.isRefreshTokenValid(refreshToken, member) && isTokenValid(refreshToken)) {

            // JWT 토큰 재발급
            String newJwtToken = generateAndSaveJwtToken(member);

            return AuthResponse.builder()
                    .accessToken(newJwtToken)
                    .refreshToken(refreshToken)
                    .build();

        } else {
            log.error("[BR ERROR]: {}", ExceptionMessage.REFRESH_TOKEN_INVALID.getText());
            throw new TokenException(ExceptionMessage.REFRESH_TOKEN_INVALID);
        }

    }

    private boolean isTokenValid(String refreshToken) {
        RefreshToken findToken = refreshTokenRepository.findById(refreshToken).orElseThrow(() -> {
            log.error("[BR ERROR]: {}", ExceptionMessage.REFRESH_TOKEN_NOT_FOUND.getText());
            throw new TokenException(ExceptionMessage.REFRESH_TOKEN_NOT_FOUND);
        });

        if (findToken.isRevoked()) {
            log.error("[BR ERROR]: {}", ExceptionMessage.REFRESH_TOKEN_REVOKED.getText());
            throw new TokenException(ExceptionMessage.REFRESH_TOKEN_REVOKED);
        }

        return true;
    }

    // 회원가입 로직
    @Transactional
    public void signUp(SignUpRequest request, MultipartFile diagnosis) {

        // 이메일 중복 확인
        validateNonExistentEmail(request.email());

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.password());

        // 회원 등록
        Member member = memberRepository.save(memberMapper.apply(request, encodedPassword));
    
        //진단서 정보 등록
        MedicalInfo medicalInfo;
        try {
            Map<String, String> extractedInfo = ocrService.extractTextFromImage(diagnosis);
            log.info("[추출된 정보]: {}", extractedInfo);
            medicalInfo = MedicalInfo.builder()
                    .member(member)
                    .diseaseInfoName(extractedInfo.get("diseaseInfoName"))
                    .diseaseInfoId(extractedInfo.get("diseaseInfoId"))
                    .hospitalInfo(extractedInfo.get("hospitalInfo"))
                    .status(VerifyStatus.PENDING)
                    .registrationID(extractedInfo.get("registrationID"))
                    .confirmationDate(extractedInfo.get("confirmationDate"))
                    .build();
            medicalInfoRepository.save(medicalInfo);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

        // 회원 그룹 생성
        List<DiseaseInfo> diseases = diseaseInfoRepository.findByCode(medicalInfo.getDiseaseInfoId()).orElseThrow(()->{
            log.error("[BR ERROR] {}", ExceptionMessage.DISEASE_NOT_FOUND.getText());
            return new DiseaseInfoException(ExceptionMessage.DISEASE_NOT_FOUND);
        });
        
        // 질병 선택 로직
        DiseaseInfo diseaseInfo;
        if (diseases.size() == 1) {
            diseaseInfo = diseases.get(0);
        }
        else {
            diseaseInfo = diseases.stream()
                    .filter(disease -> disease.getName().contains(medicalInfo.getDiseaseInfoName()))
                    .findFirst()
                    .orElseThrow(() -> {
                        log.error("[BR ERROR] {}", ExceptionMessage.DISEASE_NOT_FOUND.getText());
                        return new DiseaseInfoException(ExceptionMessage.DISEASE_NOT_FOUND);
                    });
        }
        
        // 질병 그룹화
        DiseaseMember diseaseMember = DiseaseMember.builder()
                                                   .diseaseInfo(diseaseInfo)
                                                   .member(member)
                                                   .status(DiseaseMemberStatus.MEMBER)
                                                   .build();
        diseaseMemberRepository.save(diseaseMember);

        // 회원가입 축하 메일 발송
        Events.raise(new RegisteredEvent(member.getId(), member.getEmail(), member.getNickname()));

    }

    private void validateNonExistentEmail(final String email) {
        if (memberRepository.existsByEmail(email)) {
            log.error("[BR ERROR] {} : {}", email, ExceptionMessage.MEMBER_EMAIL_ALREADY_EXIST.getText());
            throw new MemberException(ExceptionMessage.MEMBER_EMAIL_ALREADY_EXIST);
        }
    }



    BiFunction<SignUpRequest, String, Member> memberMapper = (request, encodedPassword) -> Member.builder()
            .email(request.email())
            .nickname(request.nickname())
            .password(encodedPassword)
            .role(MemberRole.MEMBER)
            .build();

    // 플랫폼에 가입된 사용자인지 확인
    public Member isMember(Member contextMember) {
        return memberRepository.findByEmail(contextMember.getEmail()).orElseThrow(() -> {
            log.error("[BR ERROR]: {}", ExceptionMessage.MEMBER_NOT_FOUND.getText());
            throw new MemberException(ExceptionMessage.MEMBER_NOT_FOUND);
        });
    }
}