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
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        logger.info("GET /api/posts - Fetching all posts");
        List<PostResponse> posts = postService.getAllPosts();
        logger.debug("Returning {} posts", posts.size());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable String id) {
        logger.info("GET /api/posts/{} - Fetching post by ID", id);
        PostResponse post = postService.getPostById(id);
        logger.debug("Found post: {}", post != null ? post.getId() : "null");
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest postRequest) {
        logger.info("POST /api/posts - Creating new post for user: {}", postRequest.getUserId());
        logger.debug("Post content: {}", postRequest.getContent());
        PostResponse createdPost = postService.createPost(postRequest);
        logger.info("Created post with ID: {}", createdPost.getId());
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
    public ResponseEntity<PostResponse> likePost(@PathVariable String id) {
        return ResponseEntity.ok(postService.likePost(id));
    }

    @PostMapping("/{id}/unlike")
    public ResponseEntity<PostResponse> unlikePost(@PathVariable String id) {
        return ResponseEntity.ok(postService.unlikePost(id));
    }
}
