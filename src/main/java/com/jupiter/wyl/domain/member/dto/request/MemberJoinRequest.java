package com.jupiter.wyl.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberJoinRequest(@NotBlank String email, String nickname, String password) {
}
