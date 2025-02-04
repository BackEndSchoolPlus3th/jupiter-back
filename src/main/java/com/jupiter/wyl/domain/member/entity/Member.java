package com.jupiter.wyl.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jupiter.wyl.global.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {

    @Column(unique = true, length = 50)
    private String email;

    @Column(unique = true, length = 50)
    private String nickname;

    @Column(length = 500)
    @JsonIgnore
    private String password;

    @Column(length = 500)
    @JsonIgnore
    private String refreshToken;
}
