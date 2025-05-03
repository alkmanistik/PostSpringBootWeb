package com.alkmanistik.springalkmanistikwebsite.service;

import com.alkmanistik.springalkmanistikwebsite.exception.NotFoundPostException;
import com.alkmanistik.springalkmanistikwebsite.model.Comment;
import com.alkmanistik.springalkmanistikwebsite.model.Post;
import com.alkmanistik.springalkmanistikwebsite.repository.CommentRepository;
import com.alkmanistik.springalkmanistikwebsite.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Transactional
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
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public void deleteByPostId(Long postId) {
        commentRepository.deleteByPostId(postId);
    }

    public List<Comment> findAllByPostId(Long postId) {
        return commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);
    }

}
