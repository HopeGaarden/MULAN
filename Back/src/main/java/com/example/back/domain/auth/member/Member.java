package com.example.back.domain.auth.member;

import com.example.back.domain.BaseEntity;
import com.example.back.domain.auth.medical.MedicalInfo;
import com.example.back.domain.auth.member.constant.MemberRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@DynamicInsert
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @OneToMany(mappedBy = "member")
    private List<MedicalInfo> medicalInfoList;

    @Column(name = "NICKNAME", nullable = false, length = 6)
    private String nickname;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "MEMBER_ROLE", nullable = false)
    @ColumnDefault(value = "'MEMBER'")
    private MemberRole role;

    @Column(name = "PROFILE_IMAGE_URL")
    private String profileImageUrl;

    @Column(name = "PUSH_ALARM_YN", nullable = false)
    private boolean pushAlarmYn = false;

    @Column(name = "SCORE", nullable = false)
    private int score = 0;

    @Builder
    public Member(String nickname, String password, String email, MemberRole role) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Spring Security UserDetails Area
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}