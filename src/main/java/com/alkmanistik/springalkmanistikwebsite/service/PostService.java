package com.alkmanistik.springalkmanistikwebsite.service;

import com.alkmanistik.springalkmanistikwebsite.exception.NotFoundPostException;
import com.alkmanistik.springalkmanistikwebsite.model.Post;
import com.alkmanistik.springalkmanistikwebsite.repository.PostRepository;
import com.alkmanistik.springalkmanistikwebsite.web.dto.PostDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@CacheConfig(cacheManager = "redisCacheManager")
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    @CacheEvict(value = "posts", allEntries = true)
    public PostDto createPost(PostDto postDto){
        if (postDto.getTitle() == null || postDto.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (postDto.getContent() == null || postDto.getContent().isBlank()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        var post = postDto.toEntity();
        return new PostDto(postRepository.save(post));
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "posts", allEntries = true),
            @CacheEvict(value = "post", key="#postId")
    })
    public PostDto updatePost(PostDto postDto, Long postId){
        if (postDto.getTitle() == null || postDto.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (postDto.getContent() == null || postDto.getContent().isBlank()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundPostException("Post not found"));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUpdatedAt(LocalDateTime.now());
        Post updatedPost = postRepository.save(post);
        return new PostDto(updatedPost);
    }

    @Cacheable(value = "post", key="#id")
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundPostException("Post not found"));

        return new PostDto(post);
    }

    @Transactional
    @CacheEvict(value = "post", key="#id")
    public void deleteById(Long id){
        postRepository.deleteById(id);
    }

    @Cacheable(value = "posts")
    public List<PostDto> getAllPosts(){
        return postRepository
                .findAllByOrderByCreatedAtDesc()
                .stream().map(PostDto::new)
                .toList();
    }

}
