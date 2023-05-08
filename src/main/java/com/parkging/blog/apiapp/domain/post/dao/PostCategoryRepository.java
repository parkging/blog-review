package com.parkging.blog.apiapp.domain.post.dao;

import com.parkging.blog.apiapp.domain.post.domain.PostCategory;
import com.parkging.blog.apiapp.domain.post.dto.PostCategoryViewDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
    Optional<PostCategory> findByName(String categoryName);
    @Query("""
              select pc.id as id
                   , pc.name as name
                   , ppc as parent
                   , count(p) as postCount
                from PostCategory pc
           left join PostCategory ppc
                  on pc.parent = ppc
           left join Post p
                  on pc = p.postCategory
               where pc.id = :categoryId
               group by pc, ppc
           """)
    Optional<PostCategoryViewDto> findViewById(Long categoryId);
    @Query("""
              select pc.id as id
                   , pc.name as name
                   , ppc as parent
                   , count(p) as postCount
                from PostCategory pc
           left join PostCategory ppc
                  on pc.parent = ppc
           left join Post p
                  on pc = p.postCategory
               group by pc, ppc
               order by COALESCE(ppc.id, pc.id), pc.id
           """)
    List<PostCategoryViewDto> findAllBy();

    Optional<PostCategory> findByParent(PostCategory postCategory);
}
