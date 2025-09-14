package com.personalisedlearn.poseidon.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    
    @Indexed(unique = true)
    private String username;
    
    private String type; // 'student' or 'teacher'
    private String avatar;
    private String bio;
    private int followers;
    private int following;
    
    // Add any additional fields and methods as needed
}
