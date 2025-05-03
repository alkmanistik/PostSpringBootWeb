package com.alkmanistik.springalkmanistikwebsite.service;

import com.alkmanistik.springalkmanistikwebsite.exception.NotFoundPostException;
import com.alkmanistik.springalkmanistikwebsite.model.Comment;
import com.alkmanistik.springalkmanistikwebsite.model.Post;
import com.alkmanistik.springalkmanistikwebsite.repository.CommentRepository;
import com.alkmanistik.springalkmanistikwebsite.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@CacheConfig(cacheManager = "redisCacheManager")
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Transactional
    @Cacheable(value = "comment", key = "#comment.id")
    public Comment createComment(Comment comment, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundPostException("Post not found"));
        if (comment.getText() == null || comment.getText().isBlank()) {
            throw new IllegalArgumentException("Comment text cannot be empty");
        }

        comment.setCreatedAt(LocalDateTime.now());
        comment.setPostId(postId);

        return commentRepository.save(comment);
    }

    @Transactional
    @CacheEvict(value = "comment", key = "#id")
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = "commentByPostId", key = "#postId")
    public void deleteByPostId(Long postId) {
        commentRepository.deleteByPostId(postId);
    }

    @Cacheable(value = "commentByPostId", key = "#postId")
    public List<Comment> findAllByPostId(Long postId) {
        return commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);
    }

}
