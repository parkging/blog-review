package com.parkging.blog.apiapp.domain.post.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {

    @NotEmpty
    private String title;
    private String content;
    @NotNull
    private Long postCategoryId;
    private String preview;

    @NotNull
    private Long memberId;

    private String thumbnailImageUrl;

    @Builder
    public PostDto(String title, String content, Long postCategoryId, String preview, Long memberId, String thumbnailImageUrl) {
        this.title = title;
        this.content = content;
        this.postCategoryId = postCategoryId;
        this.preview = preview;
        this.memberId = memberId;
        this.thumbnailImageUrl = thumbnailImageUrl;
    }
}
