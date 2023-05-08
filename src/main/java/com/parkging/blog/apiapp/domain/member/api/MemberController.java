package com.parkging.blog.apiapp.domain.member.api;

import com.parkging.blog.apiapp.domain.member.domain.Member;
import com.parkging.blog.apiapp.domain.member.domain.MemberRole;
import com.parkging.blog.apiapp.domain.member.dto.LoginDto;
import com.parkging.blog.apiapp.domain.member.dto.MemberDto;
import com.parkging.blog.apiapp.domain.member.dto.SginUpDto;
import com.parkging.blog.apiapp.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("members")
    public Long join(@RequestBody @Validated SginUpDto sginUpDto) {
        return memberService.join(sginUpDto.getName(),
                                sginUpDto.getEmail(),
                                sginUpDto.getPassword(),
                                sginUpDto.getPasswordConfirm(),
                                MemberRole.ROLE_USER);
    }

    @Secured({MemberRole.ROLES.USER, MemberRole.ROLES.ADMIN})
    @GetMapping("members/{memberId}")
    public MemberDto getMember(@PathVariable Long memberId) {
        Member member = memberService.findById(memberId);
        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }


    /**
     * /certification => /login 으로 변경; 더이상 사용한함!
     * Post /login 호출 시 Spring Security Filter(JwtAuthenticationFilter) 에서 인증 처리로 변경
     */
//    @PostMapping("certification")
    public String login(@RequestBody @Validated LoginDto loginDto) {

        return "";
    }

    @Secured({MemberRole.ROLES.USER, MemberRole.ROLES.ADMIN})
    @PatchMapping("members/{memberId}")
    public Long updateById(@PathVariable(required = true) Long memberId, @RequestBody MemberDto memberDto) {
        return memberService.update(memberId,
                            memberDto.getName(),
                            memberDto.getPassword(),
                            MemberRole.ROLE_USER);
    }

    /**
     * JWT로 변경 => logout 기능 더이상 사용안함!
     */
//    @DeleteMapping("certification")
    public void logout() {
    }

}
