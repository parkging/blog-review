package com.parkging.blog.apiapp.global.oauth.service;

import com.parkging.blog.apiapp.domain.member.domain.Member;
import com.parkging.blog.apiapp.domain.member.domain.MemberRole;
import com.parkging.blog.apiapp.domain.member.service.MemberService;
import com.parkging.blog.apiapp.global.auth.PrincipalDetails;
import com.parkging.blog.apiapp.global.oauth.OAuthAttributes;
import com.parkging.blog.apiapp.global.oauth.util.OAuthUtil;
import com.parkging.blog.apiapp.global.oauth.userinfo.OAuth2UserInfo;
import com.parkging.blog.apiapp.global.oauth.userinfo.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final OAuthUtil oauthUtil;

    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("PrincipalOauth2UserService.loadUser");

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = oauthUtil.getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 로그인 시 키(PK)가 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보들)

        // socialType에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);
        log.info("oauth2user={}", extractAttributes.getOauth2UserInfo().toString());

        // 추출된 extractAttributes로 회원 조회; 회원 미존재 시 회원가입
        Member member = getMember(extractAttributes, socialType);

        return new PrincipalDetails(member, attributes);

    }

    private Member getMember(OAuthAttributes extractAttributes, SocialType socialType) {

        OAuth2UserInfo oauth2UserInfo = extractAttributes.getOauth2UserInfo();
        Member member = null;

        try {
            member = memberService.findByEmail(oauth2UserInfo.getEmail());
        } catch (NoResultException e) {
            String password = oauth2UserInfo.getId() + oauth2UserInfo.getEmail();
            Long memberId = memberService.join(oauth2UserInfo.getName(),
                    oauth2UserInfo.getEmail(),
                    password,
                    password,
                    MemberRole.ROLE_USER);

            member = memberService.findById(memberId);
        }

        return member;
    }
}
