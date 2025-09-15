package com.personalisedlearn.poseidon.service;

import com.personalisedlearn.poseidon.dto.PostRequest;
import com.personalisedlearn.poseidon.dto.PostResponse;
import com.personalisedlearn.poseidon.exception.ResourceNotFoundException;
import com.personalisedlearn.poseidon.model.Post;
import com.personalisedlearn.poseidon.model.User;
import com.personalisedlearn.poseidon.repository.PostRepository;
import com.personalisedlearn.poseidon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.personalisedlearn.poseidon.mapper.PostMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    public List<PostResponse> getAllPosts(String currentUsername) {
        try {
            // Get all posts sorted by creation date in descending order (newest first)
            List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
            if (posts == null || posts.isEmpty()) {
                return Collections.emptyList();
            }
            // If no username is provided, still return posts but with all likes set to false
            return posts.stream()
                    .map(post -> convertToResponse(post, currentUsername))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching posts", e);
            throw e;
        }
    }

    public PostResponse getPostById(String id, String currentUsername) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        return convertToResponse(post, currentUsername);
    }

    public PostResponse createPost(PostRequest postRequest) {
        User user = userRepository.findByUsername(postRequest.getUserName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + postRequest.getUserName()));
        
        Post post = postMapper.toEntity(postRequest);
        post.setUser(user);
        post.setTimestamp(LocalDateTime.now());
        
        Post savedPost = postRepository.save(post);
        return convertToResponse(savedPost, postRequest.getUserName());
    }

    public PostResponse updatePost(String id, PostRequest postRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        
        postMapper.updateEntityFromDto(postRequest, post);
        Post updatedPost = postRepository.save(post);
        return convertToResponse(updatedPost, postRequest.getUserName());
    }

    public void deletePost(String id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }

    public PostResponse toggleLike(String postId, String username) {
        logger.info("Starting toggleLike for postId: {}, username: {}", postId, username);
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    logger.error("Post not found with id: {}", postId);
                    return new ResourceNotFoundException("Post not found with id: " + postId);
                });
        
        logger.info("Found post with id: {}, current likes: {}", postId, post.getLikes());
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found with username: {}", username);
                    return new ResourceNotFoundException("User not found with username: " + username);
                });
        
        logger.info("Found user with id: {}, username: {}", user.getId(), user.getUsername());
        
        // Log current likedByUsers before toggle
        logger.info("Current likedByUsers before toggle: {}", 
            post.getLikedByUsers().stream()
                .map(u -> u.getUsername() + "(" + u.getId() + ")")
                .collect(Collectors.joining(", ")));
        
        // Toggle like status
        post.toggleLike(user);
        
        logger.info("After toggle - likedByUsers: {}", 
            post.getLikedByUsers().stream()
                .map(u -> u.getUsername() + "(" + u.getId() + ")")
                .collect(Collectors.joining(", ")));
        
        Post updatedPost = postRepository.save(post);
        logger.info("Saved post. New like count: {}", updatedPost.getLikes());
        
        PostResponse response = convertToResponse(updatedPost, username);
        logger.info("Converted to response. Response like count: {}, liked: {}", 
            response.getLikes(), response.isLiked());
            
        return response;
    }
    
    private PostResponse convertToResponse(Post post, String currentUsername) {
        PostResponse response = postMapper.toResponse(post);
        // Set liked status based on current user
        if (currentUsername != null && !currentUsername.isEmpty()) {
            response.setLiked(post.isLikedByUser(currentUsername));
        } else {
            response.setLiked(false);
        }
        return response;
    }
}
