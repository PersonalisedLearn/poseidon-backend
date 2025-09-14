package com.personalisedlearn.poseidon.dto;

import com.personalisedlearn.poseidon.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String name;
    private String username;
    private String type;
    private String avatar;
    private String bio;
    private int followers;
    private int following;
    private Gender gender;
}
