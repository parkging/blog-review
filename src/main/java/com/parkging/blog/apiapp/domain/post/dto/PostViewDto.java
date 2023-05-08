package com.parkging.blog.apiapp.domain.post.dto;

import com.parkging.blog.apiapp.domain.member.domain.Member;
import com.parkging.blog.apiapp.domain.post.domain.PostCategory;

import java.time.LocalDateTime;

public interface PostViewDto {
    Long getId();
    String getTitle();
    String getContent();
    String getPostCategoryName();
    String getPostCategoryId();
    String getPreview();
    String getMemberName();
    String getMemberId();
    String getThumbnailImageUrl();
    LocalDateTime getCreateDate();
    LocalDateTime getModifiedDate();
}
