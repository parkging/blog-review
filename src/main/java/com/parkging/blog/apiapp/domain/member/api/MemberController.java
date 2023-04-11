package com.parkging.blog.apiapp.domain.member.api;

import com.parkging.blog.apiapp.domain.member.domain.Member;
import com.parkging.blog.apiapp.domain.member.domain.MemberRole;
import com.parkging.blog.apiapp.domain.member.dto.LoginDto;
import com.parkging.blog.apiapp.domain.member.dto.LoginResult;
import com.parkging.blog.apiapp.domain.member.dto.MemberDto;
import com.parkging.blog.apiapp.domain.member.dto.SginUpDto;
import com.parkging.blog.apiapp.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("members")
    public Long join(@RequestBody @Validated SginUpDto sginUpDto) {
        return memberService.join(sginUpDto.getName(),
                                sginUpDto.getEmail(),
                                bCryptPasswordEncoder.encode(sginUpDto.getPassword()),
                                bCryptPasswordEncoder.encode(sginUpDto.getPasswordConfirm()),
                                MemberRole.ROLE_USER);
    }

    @Secured({MemberRole.ROLES.USER})
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

    @Secured({MemberRole.ROLES.USER})
    @PatchMapping("members/{memberId}")
    public Long updateById(@PathVariable(required = true) Long memberId, @RequestBody MemberDto memberDto) {
        return memberService.update(memberId,
                            memberDto.getName(),
                            bCryptPasswordEncoder.encode(memberDto.getPassword()),
                            MemberRole.ROLE_USER);
    }

    @DeleteMapping("certification")
    public void logout() {
        /* todo 로그아웃 기능 추가 필요 */
    }

}
