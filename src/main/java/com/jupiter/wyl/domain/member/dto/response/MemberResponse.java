package com.jupiter.wyl.domain.member.dto.response;

import com.jupiter.wyl.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberResponse {
    private String email;
    private String nickname;

    public MemberResponse(Member member) {
        this.nickname = member.getNickname();
        this.email = member.getEmail();
    }
}
