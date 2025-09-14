package com.personalisedlearn.poseidon.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostRequest {
    @NotBlank(message = "Username is required")
    private String userName;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    private MediaRequest media;
    
    @Data
    public static class MediaRequest {
        private String url;
        private String type; // 'image' or 'video'
    }
}
