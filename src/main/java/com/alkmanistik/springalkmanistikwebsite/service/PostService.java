package com.alkmanistik.springalkmanistikwebsite.service;

import com.alkmanistik.springalkmanistikwebsite.exception.NotFoundPostException;
import com.alkmanistik.springalkmanistikwebsite.model.Post;
import com.alkmanistik.springalkmanistikwebsite.repository.PostRepository;
import com.alkmanistik.springalkmanistikwebsite.web.dto.PostDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
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
    public PostDto updatePost(PostDto postDto){
        if (postDto.getTitle() == null || postDto.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (postDto.getContent() == null || postDto.getContent().isBlank()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        Post post = postRepository.findById(postDto.getId())
                .orElseThrow(() -> new NotFoundPostException("Post not found"));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUpdatedAt(LocalDateTime.now());
        Post updatedPost = postRepository.save(post);
        return new PostDto(updatedPost);
    }


    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundPostException("Post not found"));

        return new PostDto(post);
    }

    @Transactional
    public void deleteById(Long id){
        postRepository.deleteById(id);
    }

    public List<PostDto> getAllPosts(){
        return postRepository
                .findAllByOrderByCreatedAtDesc()
                .stream().map(PostDto::new)
                .toList();
    }

}
