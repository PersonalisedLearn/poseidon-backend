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

    public List<PostResponse> getAllPosts() {
        try {
            List<Post> posts = postRepository.findAll();
            if (posts == null || posts.isEmpty()) {
                return Collections.emptyList();
            }
            return posts.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching posts", e);
            throw e;
        }
    }

    public PostResponse getPostById(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        return convertToResponse(post);
    }

    public PostResponse createPost(PostRequest postRequest) {
        User user = userRepository.findByUsername(postRequest.getUserName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + postRequest.getUserName()));
        
        Post post = postMapper.toEntity(postRequest);
        post.setUser(user);
        post.setTimestamp(LocalDateTime.now());
        
        Post savedPost = postRepository.save(post);
        return convertToResponse(savedPost);
    }

    public PostResponse updatePost(String id, PostRequest postRequest) {
        return postRepository.findById(id)
                .map(post -> {
                    postMapper.updateEntityFromDto(postRequest, post);
                    Post updatedPost = postRepository.save(post);
                    return convertToResponse(updatedPost);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
    }

    public void deletePost(String id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }

    public PostResponse likePost(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        
        post.setLikes(post.getLikes() + 1);
        Post updatedPost = postRepository.save(post);
        return convertToResponse(updatedPost);
    }
    
    public PostResponse unlikePost(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        
        if (post.getLikes() > 0) {
            post.setLikes(post.getLikes() - 1);
            Post updatedPost = postRepository.save(post);
            return convertToResponse(updatedPost);
        }
        
        return convertToResponse(post);
    }
    
    private PostResponse convertToResponse(Post post) {
        PostResponse response = postMapper.toResponse(post);
        // Set liked status based on current user (if available)
        // You'll need to implement proper authentication to get the current user
        response.setLiked(false);
        return response;
    }
}
