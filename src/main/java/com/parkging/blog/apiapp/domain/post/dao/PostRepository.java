package com.parkging.blog.apiapp.domain.post.dao;

import com.parkging.blog.apiapp.domain.post.domain.Post;
import com.parkging.blog.apiapp.domain.post.domain.PostCategory;
import com.parkging.blog.apiapp.domain.post.dto.PostViewDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<PostViewDto> findPostAndMemberByPostCategory(PostCategory postCategory, Pageable pageable);
    List<PostViewDto> findPostAndMemberBy(Pageable pageable);
    Optional<PostViewDto> findViewById(Long postId);
    Long countByPostCategoryName(String postCategoryName);

}
