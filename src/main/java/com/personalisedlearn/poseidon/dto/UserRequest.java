package com.personalisedlearn.poseidon.dto;

import com.personalisedlearn.poseidon.model.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "User type is required")
    private String type; // 'student' or 'teacher'
    
    private String avatar;
    private String bio;
    private int followers;
    private int following;
    private Gender gender;
}
