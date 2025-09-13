package com.personalisedlearn.poseidon.repository;

import com.personalisedlearn.poseidon.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}
