package com.parkging.blog.apiapp.global.oauth;

import com.parkging.blog.apiapp.global.oauth.userinfo.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.boot.model.naming.IllegalIdentifierException;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private String nameAttributeKey;
    private OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    public static OAuthAttributes of(SocialType socialType,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {

        if(socialType.equals(SocialType.GOOGLE)) {
            return ofGoogle(userNameAttributeName, attributes);
        } else if(socialType.equals(SocialType.NAVER)) {
            return ofNaver(userNameAttributeName, attributes);
        } else if(socialType.equals(SocialType.KAKAO)) {
            return ofKakao(userNameAttributeName, attributes);
        } else {
            throw new IllegalIdentifierException("지원하지 않는 소셜 로그인 요청입니다.");
        }
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

}
