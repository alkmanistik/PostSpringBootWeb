package com.alkmanistik.springalkmanistikwebsite.repository;

import com.alkmanistik.springalkmanistikwebsite.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostId(Long postId);

    List<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId);

    void deleteByPostId(Long postId);

}
