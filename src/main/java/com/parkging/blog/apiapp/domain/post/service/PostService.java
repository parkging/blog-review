package com.parkging.blog.apiapp.domain.post.service;

import com.parkging.blog.apiapp.domain.member.service.MemberService;
import com.parkging.blog.apiapp.domain.post.dao.PostRepository;
import com.parkging.blog.apiapp.domain.post.domain.Post;
import com.parkging.blog.apiapp.domain.post.domain.PostCategory;
import com.parkging.blog.apiapp.domain.post.dto.PostViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {
    public static final int MAX_PREVIEW_SIZE = 100;
    private final PostRepository postRepository;
    private final PostCategoryService postCategoryService;
    private final MemberService memberService;

    @Transactional
    public Long save(String title,
                     String content,
                     Long postCategoryId,
                     String preview,
                     Long memberId,
                     String thumbnailImageUrl) {
        return postRepository.save(Post.builder()
                    .title(title)
                    .content(content)
                    .postCategory(postCategoryService.findById(postCategoryId))
                    .preview(getPreview(content))
                    .member(memberService.findById(memberId))
                    .thumbnailImageUrl(thumbnailImageUrl)
                    .build())
                .getId();
    }

    @Transactional
    public Long update(Long postId,
                       Long postCategoryId,
                       String title,
                       String content,
                       String preview,
                       String thumbnailImageUrl) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("error.post.notexgist"));
        return post.update(postCategoryService.findById(postCategoryId),
                            title,
                            content,
                            preview,
                            thumbnailImageUrl);
    }

    @Transactional
    public Long deleteById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("error.post.notexgist"));
        postRepository.deleteById(post.getId());
        return post.getId();
    }

    public List<PostViewDto> findAllViewByCategoryName(String categoryName, Pageable pageable) {
        PostCategory postCategory = postCategoryService.findByName(categoryName);
        return postRepository.findPostAndMemberByPostCategory(postCategory, pageable);
    }

    public List<PostViewDto> findAll(Pageable pageable) {
        return postRepository.findPostAndMemberBy(pageable);
    }

    public PostViewDto findById(Long postId) {
        return postRepository.findViewById(postId)
                .orElseThrow(() -> new NoSuchElementException("error.post.notexgist"));
    }

    public Long countAll() {
        return postRepository.count();
    }

    public Long countByPostCategoryName(String postCategoryName) {
        return postRepository.countByPostCategoryName(postCategoryName);
    }

    /**********************************비지니스 로직 분리**********************************/

    private String getPreview(String content) {
        String tagRemovedContent = content
                //이미지 링크 태그 제거
                .replaceAll("\\!\\[.*?\\)", "")
                //html 태그 제거
                .replaceAll("\\<.*?\\>", "")
                //특수문자 제거
                .replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9 .]", "");

        //content에서 MAX_PREVIEW_SIZE 만큼 글자수 제한
        String preview = tagRemovedContent.substring(0,
                tagRemovedContent.length() < MAX_PREVIEW_SIZE ? tagRemovedContent.length() : MAX_PREVIEW_SIZE);
        return preview;
    }

}
