package com.personalisedlearn.poseidon.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    
    @DBRef
    private Set<User> likedByUsers = new HashSet<>();
    
    @Data
    public static class Media {
        private String url;
        private String type; // 'image' or 'video'
    }
    
    public boolean isLikedByUser(String username) {
        return likedByUsers.stream().anyMatch(user -> user.getUsername().equals(username));
    }
    
    public void toggleLike(User user) {
        System.out.println("Toggling like for user: " + user.getUsername() + " (ID: " + user.getId() + ")");
        System.out.println("Current likedByUsers before toggle: " + 
            likedByUsers.stream()
                .map(u -> u.getUsername() + "(" + u.getId() + ")")
                .collect(Collectors.joining(", ")));
        
        // Find if the user already liked the post by username
        boolean alreadyLiked = likedByUsers.stream()
                .anyMatch(u -> u.getUsername().equals(user.getUsername()));
                
        System.out.println("User already liked the post: " + alreadyLiked);
                
        if (alreadyLiked) {
            // Remove the user from the set
            boolean removed = likedByUsers.removeIf(u -> u.getUsername().equals(user.getUsername()));
            likes = Math.max(0, likes - 1);
            System.out.println("Removed user like. Success: " + removed);
        } else {
            // Add the user to the set
            boolean added = likedByUsers.add(user);
            likes++;
            System.out.println("Added user like. Success: " + added);
        }
        
        System.out.println("Current likedByUsers after toggle: " + 
            likedByUsers.stream()
                .map(u -> u.getUsername() + "(" + u.getId() + ")")
                .collect(Collectors.joining(", ")));
        System.out.println("New like count: " + likes);
    }
}
