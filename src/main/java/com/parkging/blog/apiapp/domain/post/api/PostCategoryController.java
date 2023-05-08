package com.parkging.blog.apiapp.domain.post.api;

import com.parkging.blog.apiapp.domain.member.domain.MemberRole;
import com.parkging.blog.apiapp.domain.post.dto.PostCategoryDto;
import com.parkging.blog.apiapp.domain.post.dto.PostCategoryViewDto;
import com.parkging.blog.apiapp.domain.post.service.PostCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostCategoryController {
    private final PostCategoryService postCategoryService;

    @Secured(MemberRole.ROLES.ADMIN)
    @PostMapping("postcategories")
    public Long savePostCategory(@RequestBody @Validated PostCategoryDto postCategoryDto) {
        return postCategoryService.save(postCategoryDto.getName(),
                postCategoryDto.getParentPostCategoryId());
    }

    @GetMapping("postcategories/{postCategoryId}")
    public PostCategoryViewDto findViewById(@PathVariable(required = true) Long postCategoryId) {
        return postCategoryService.findViewById(postCategoryId);
    }

    @GetMapping("postcategories")
    public List<PostCategoryViewDto> findAllView() {
        return postCategoryService.findAllView();
    }

    @Secured(MemberRole.ROLES.ADMIN)
    @PatchMapping("postcategories/{postCategoryId}")
    public Long updateById(@PathVariable(required = true) Long postCategoryId,
                           @RequestBody @Validated PostCategoryDto postCategoryDto) {
        return postCategoryService.update(postCategoryId,
                postCategoryDto.getName(),
                postCategoryDto.getParentPostCategoryId());
    }

    @Secured(MemberRole.ROLES.ADMIN)
    @DeleteMapping("postcategories/{postCategoryId}")
    public Long deleteById(@PathVariable(required = true) Long postCategoryId) {
        return postCategoryService.deleteById(postCategoryId);
    }
}
