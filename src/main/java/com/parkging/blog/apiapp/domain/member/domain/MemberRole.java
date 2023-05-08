package com.parkging.blog.apiapp.domain.member.domain;

public enum MemberRole {
    ROLE_USER(ROLES.USER){
        @Override
        public String toString() {
            return "ROLE_USER";
        }
    },
    ROLE_ADMIN(ROLES.ADMIN) {
        @Override
        public String toString() {
            return "ROLE_ADMIN";
        }
    };

    private String auth;

    private MemberRole(String auth) {
        this.auth = auth;
    }

    public static class ROLES {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";

    }
}
