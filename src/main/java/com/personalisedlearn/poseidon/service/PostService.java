package com.personalisedlearn.poseidon.service;

import com.personalisedlearn.poseidon.model.Post;
import com.personalisedlearn.poseidon.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(String id) {
        return postRepository.findById(id);
    }

    public Post createPost(Post post) {
        post.setId(UUID.randomUUID().toString());
        return postRepository.save(post);
    }

    public Post updatePost(String id, Post postDetails) {
        postDetails.setId(id);
        return postRepository.save(postDetails);
    }

    public void deletePost(String id) {
        postRepository.deleteById(id);
    }
}
