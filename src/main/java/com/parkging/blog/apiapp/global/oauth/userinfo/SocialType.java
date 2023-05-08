package com.parkging.blog.apiapp.global.oauth.userinfo;

public enum SocialType {
    KAKAO(TYPE.KAKAO) {
        @Override
        public String toString() {
            return TYPE.KAKAO;
        }
    }, NAVER(TYPE.NAVER) {
        @Override
        public String toString() {
            return TYPE.NAVER;
        }
    }, GOOGLE(TYPE.GOOGLE) {
        @Override
        public String toString() {
            return TYPE.GOOGLE;
        }
    };

    String socialType;

    SocialType(String socialType) {
        this.socialType = socialType;
    }

    public static class TYPE {
        public static final String KAKAO = "kakao";
        public static final String NAVER = "naver";
        public static final String GOOGLE = "google";

    }

}
