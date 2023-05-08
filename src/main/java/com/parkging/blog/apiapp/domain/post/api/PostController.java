package com.parkging.blog.apiapp.domain.post.api;

import com.parkging.blog.apiapp.domain.member.domain.MemberRole;
import com.parkging.blog.apiapp.domain.post.dto.PostDto;
import com.parkging.blog.apiapp.domain.post.dto.PostViewDto;
import com.parkging.blog.apiapp.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    public static final int PAGE_SIZE = 5;
    private final PostService postService;

    @Secured({MemberRole.ROLES.USER, MemberRole.ROLES.ADMIN})
    @PostMapping("posts")
    public Long savePost(@RequestBody @Validated PostDto postDto) {
        return postService.save(postDto.getTitle(),
                    postDto.getContent(),
                    postDto.getPostCategoryId(),
                    postDto.getPreview(),
                    postDto.getMemberId(),
                    postDto.getThumbnailImageUrl());
    }


    @GetMapping("posts")
    public List<PostViewDto> findAll(@PageableDefault(size = PAGE_SIZE,
            sort = "id",
            direction = Sort.Direction.DESC)
                                     Pageable pageable,
                                     HttpServletResponse response,
                                     @RequestParam(required = false) String postCategoryName
    ) {
        if(StringUtils.isEmpty(postCategoryName)) {
            response.setHeader("X-Total-Count", Long.toString(postService.countAll()));
            return postService.findAll(pageable);
        } else {
            response.setHeader("X-Total-Count", Long.toString(postService.countByPostCategoryName(postCategoryName)));
            return postService.findAllViewByCategoryName(postCategoryName, pageable);
        }
    }

    @GetMapping("posts/{postId}")
    public PostViewDto findById(@PathVariable(required = true) Long postId) {
        return postService.findById(postId);
    }

//    @GetMapping("/posts")
//    public List<PostViewDto> findByCategoryName(@PathVariable(required = true) String categoryName,
//                                      @PageableDefault(size = PAGE_SIZE,
//                                       sort = "id",
//                                       direction = Sort.Direction.DESC)
//                               Pageable pageable) {
//        return postService.findAllViewByCategoryName(categoryName, pageable);
//    }

    @Secured({MemberRole.ROLES.USER, MemberRole.ROLES.ADMIN})
    @PatchMapping("posts/{postId}")
    public Long updateById(@PathVariable(required = true) Long postId,
                               @RequestBody @Validated PostDto postDto) {
        return postService.update(postId,
                postDto.getPostCategoryId(),
                postDto.getTitle(),
                postDto.getContent(),
                postDto.getPreview(),
                postDto.getThumbnailImageUrl());
    }

    @Secured({MemberRole.ROLES.USER, MemberRole.ROLES.ADMIN})
    @DeleteMapping("posts/{postId}")
    public Long deletetById(@PathVariable(required = true) Long postId) {
        return postService.deleteById(postId);
    }

}
