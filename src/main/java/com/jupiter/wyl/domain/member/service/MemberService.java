package com.jupiter.wyl.domain.member.service;

import com.jupiter.wyl.domain.member.entity.Member;
import com.jupiter.wyl.domain.member.repository.MemberRepository;
import com.jupiter.wyl.global.exception.ExceptionCode;
import com.jupiter.wyl.global.exception.ServiceException;
import com.jupiter.wyl.global.jwt.JwtProvider;
import com.jupiter.wyl.global.rsData.RsData;
import com.jupiter.wyl.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    // 비밀번호 검증
    // 사용자가 입력한 비밀번호(rawPassword)와 저장된 비밀번호(encodedPassword) 비교
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    // 회원가입
    public Member join(String email, String nickname, String password) {
        Member CheckedSignUpMember = memberRepository.findByEmail(email).orElse(null);
        Member CheckedSignUpMemberNickname = memberRepository.findByNickname(nickname).orElse(null);

        if (CheckedSignUpMember != null) {
            throw new ServiceException(ExceptionCode.EMAIL_ALREADY_REGISTERED);
        } else if (CheckedSignUpMemberNickname != null) {
            nickname="이름없는 어피치";
//            throw new ServiceException(ExceptionCode.NICKNAME_ALREADY_TAKEN); // 닉네임은 중복이어도 무관, 현재 서비스 회원가입 창에 닉네임을 별도로 설정하는 곳이 없으므로 주석 처리.
        }

        Member member = Member.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .password(passwordEncoder.encode(password))
                .build();

        String refreshToken = jwtProvider.genRefreshToken(member);
        member.setRefreshToken(refreshToken);

        return memberRepository.save(member);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        return jwtProvider.verify(token);
    }

    // 토큰 갱신
    public RsData<String> refreshAccessToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));
        String accessToken = jwtProvider.genAccessToken(member);
        return new RsData<>("200", "토큰 갱신 성공", accessToken);
    }

    // 토큰으로 User 정보 가져오기
    public SecurityUser getUserFromAccessToken(String accessToken) {
        Map<String, Object> payloadBody = jwtProvider.getClaims(accessToken);
        long id = (int) payloadBody.get("id");
        String nickname = (String) payloadBody.get("nickname");
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new SecurityUser(id, nickname, "", authorities);
    }

    // 토큰으로 User의 이메일 정보 가져오기
    public SecurityUser getEmailFromAccessToken(String accessToken) {
        Map<String, Object> payloadBody = jwtProvider.getClaims(accessToken);
        long id = (int) payloadBody.get("id");
        String email = (String) payloadBody.get("email");
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new SecurityUser(id, email, "", authorities);
    }

    // 멤버 정보
    public String getUserLikeGenres(String email){
        return memberRepository.findByEmail(email).get().getLikeGenres();
    }
}
