package com.parkging.blog.apiapp.domain.post.service;

import com.parkging.blog.apiapp.domain.post.dao.PostCategoryRepository;
import com.parkging.blog.apiapp.domain.post.domain.PostCategory;
import com.parkging.blog.apiapp.domain.post.dto.PostCategoryViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostCategoryService {
    private final PostCategoryRepository postCategoryRepository;

    @Transactional
    public Long save(String name, Long parentPostCategoryId) {
        if(parentPostCategoryId == null) parentPostCategoryId = 0L;

        return postCategoryRepository.save(PostCategory.builder()
                            .name(name)
                            .parent(postCategoryRepository.findById(parentPostCategoryId).orElse(null))
                            .build())
                .getId();
    }

    @Transactional
    public Long update(Long postCategoryId, String name, Long parentPostCategoryId) {
        if(parentPostCategoryId == null) parentPostCategoryId = 0L;

        PostCategory postCategory = postCategoryRepository.findById(postCategoryId)
                .orElseThrow(() -> new NoSuchElementException("error.postcategory.notexgist"));
        PostCategory parent = postCategoryRepository.findById(parentPostCategoryId)
                .orElse(null);
        return postCategory.update(name, parent);
    }

    @Transactional
    public Long deleteById(Long postCategoryId) {
        PostCategory postCategory = postCategoryRepository.findById(postCategoryId)
                .orElseThrow(() -> new NoSuchElementException("error.postcategory.notexgist"));
        postCategoryRepository.delete(postCategory);
        return postCategory.getId();
    }

    public PostCategory findByName(String postCategoryName) {
        return postCategoryRepository.findByName(postCategoryName)
                .orElseThrow(() -> new NoSuchElementException("error.postcategory.notexgist"));
    }

    public PostCategory findById(Long postCategoryId) {
        return postCategoryRepository.findById(postCategoryId)
                .orElseThrow(() -> new NoSuchElementException("error.postcategory.notexgist"));
    }

    public PostCategoryViewDto findViewById(Long postCategoryId) {
        return postCategoryRepository.findViewById(postCategoryId)
                .orElseThrow(() -> new NoSuchElementException("error.postcategory.notexgist"));
    }

    public List<PostCategoryViewDto> findAllView() {
        return postCategoryRepository.findAllBy();
    }
}
