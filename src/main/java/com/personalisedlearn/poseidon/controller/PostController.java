package com.personalisedlearn.poseidon.controller;

import com.personalisedlearn.poseidon.dto.PostRequest;
import com.personalisedlearn.poseidon.dto.PostResponse;
import com.personalisedlearn.poseidon.service.PostService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
        logger.info("PostController initialized");
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(
            @RequestParam(value = "username", required = false) String username) {
        logger.info("GET /api/posts - Fetching all posts for user: {}", username);
        List<PostResponse> posts = postService.getAllPosts(username);
        logger.debug("Returning {} posts", posts != null ? posts.size() : 0);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(
            @PathVariable String id,
            @RequestParam(required = false) String username) {
        logger.info("GET /api/posts/{} - Fetching post by ID for user: {}", id, username);
        PostResponse post = postService.getPostById(id, username);
        logger.debug("Found post: {}", post != null ? post.getId() : "null");
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest postRequest) {
        logger.info("POST /api/posts - Creating new post for username: {}", postRequest.getUserName());
        logger.debug("Post content: {}", postRequest.getContent());
        PostResponse createdPost = postService.createPost(postRequest);
        logger.info("Created post with ID: {} for username: {}", createdPost.getId(), postRequest.getUserName());
        return new ResponseEntity<>(
                createdPost,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable String id,
            @Valid @RequestBody PostRequest postRequest
    ) {
        return ResponseEntity.ok(postService.updatePost(id, postRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<PostResponse> toggleLike(
            @PathVariable("id") String id,
            @RequestParam("username") String username) {
        logger.info("Toggling like for post: {} by user: {}", id, username);
        try {
            PostResponse response = postService.toggleLike(id, username);
            logger.info("Successfully toggled like. New like count: {}", response.getLikes());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error toggling like for post: {} by user: {}", id, username, e);
            throw e;
        }
    }
}
