package com.parkging.blog.apiapp.domain.member.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {
    @NotEmpty
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty @Email
    private String email;

    private String password;

    @Builder
    public MemberDto(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
