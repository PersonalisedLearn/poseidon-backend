package com.personalisedlearn.poseidon.repository;

import com.personalisedlearn.poseidon.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PostRepository extends MongoRepository<Post, String> {
    long deleteByUserId(String userId);
    
    @Query(value = "{'user.$id': ?0}", delete = true)
    void deleteAllByUserId(String userId);
}
