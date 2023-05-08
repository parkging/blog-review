package com.parkging.blog.apiapp.domain.post.dto;

import com.parkging.blog.apiapp.domain.post.domain.PostCategory;

public interface PostCategoryViewDto {
    Long getId();
    String getName();
    PostCategory getParent();
    Long getPostCount();
}
