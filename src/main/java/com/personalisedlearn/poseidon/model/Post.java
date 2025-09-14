package com.personalisedlearn.poseidon.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Data
@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    
    @DBRef
    private User user;
    
    private String content;
    private LocalDateTime timestamp;
    private int likes;
    private int comments;
    private Media media;
    
    @Data
    public static class Media {
        private String url;
        private String type; // 'image' or 'video'
    }
}
