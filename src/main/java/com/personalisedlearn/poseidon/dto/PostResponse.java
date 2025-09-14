package com.personalisedlearn.poseidon.dto;

import com.personalisedlearn.poseidon.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private String id;
    private UserResponse user;
    private String content;
    private LocalDateTime timestamp;
    private int likes;
    private int comments;
    private MediaResponse media;
    private boolean isLiked;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponse {
        private String id;
        private String name;
        private String username;
        private String type;
        private String avatar;
        private String bio;
        private int followers;
        private int following;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MediaResponse {
        private String url;
        private String type;
    }
}
