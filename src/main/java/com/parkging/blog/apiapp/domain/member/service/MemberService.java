package com.parkging.blog.apiapp.domain.member.service;

import com.parkging.blog.apiapp.domain.member.dao.MemberRepository;
import com.parkging.blog.apiapp.domain.member.domain.Member;
import com.parkging.blog.apiapp.domain.member.domain.MemberRole;
import com.parkging.blog.apiapp.domain.member.exception.LoginFailException;
import com.parkging.blog.apiapp.domain.member.exception.UnavailableSignUpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    public static final boolean LOGIN_REJECT = true;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Long join(String name, String email, String password, String passwordConfirm, MemberRole memberRole) {
        //회원가입 할 수 없도록 임시 수정
        if(LOGIN_REJECT) {
            throw new UnavailableSignUpException("error.member.unavailablesignup");
        }

        passwordValidationCheck(password, passwordConfirm);
        emailValidationCheck(email);

        return memberRepository.save(Member.builder()
                .name(name)
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .memberRole(MemberRole.ROLE_USER)
                .build()
            ).getId();
    }

    @Transactional
    public Long update(Long id, String name, String password, MemberRole memberRole) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NoResultException("error.member.notexgist"));

        member.update(name, bCryptPasswordEncoder.encode(password), memberRole);

        return member.getId();
    }

    public Member login(String email, String password) {
        return memberRepository.findByEmail(email)
                .filter(member -> member.getPassword().equals(password))
                .orElseThrow(() -> new LoginFailException("error.login.fail"));
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoResultException("error.member.notexgist"));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoResultException("error.member.notexgist"));
    }

    /************************비지니스 로직 분리************************/

    private void passwordValidationCheck(String password, String passwordConfirm) {
        if(!password.equals(passwordConfirm)) {
            throw new IllegalArgumentException("error.password.incorect");
        }
    }

    private void emailValidationCheck(String email) {
        if(memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("error.email.exgist");
        }
    }


}
