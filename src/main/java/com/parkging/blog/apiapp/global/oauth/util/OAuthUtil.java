package com.parkging.blog.apiapp.global.oauth.util;

import com.parkging.blog.apiapp.global.oauth.userinfo.SocialType;
import org.springframework.stereotype.Component;

@Component
public class OAuthUtil {

    public SocialType getSocialType(String registrationId) {

        if(SocialType.TYPE.NAVER.equals(registrationId)) {
            return SocialType.NAVER;
        } else if(SocialType.TYPE.KAKAO.equals(registrationId)) {
            return SocialType.KAKAO;
        } else if(SocialType.TYPE.GOOGLE.equals(registrationId)) {
            return SocialType.GOOGLE;
        } else {
            throw new IllegalArgumentException("지원하지 않는 소셜 로그인 요청입니다.");
        }

    }
}
