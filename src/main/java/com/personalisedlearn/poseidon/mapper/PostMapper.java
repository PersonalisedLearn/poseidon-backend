package com.personalisedlearn.poseidon.mapper;

import com.personalisedlearn.poseidon.dto.PostRequest;
import com.personalisedlearn.poseidon.dto.PostResponse;
import com.personalisedlearn.poseidon.model.Post;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Mapper(componentModel = "spring",
        uses = {UserMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PostMapper extends BaseMapper<PostRequest, Post> {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "likes", constant = "0")
    @Mapping(target = "comments", constant = "0")
    @Override
    Post toEntity(PostRequest dto);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "isLiked", expression = "java(false)") // Default value, can be set later
    PostResponse toResponse(Post entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Override
    void updateEntityFromDto(PostRequest dto, @MappingTarget Post entity);

    @AfterMapping
    default void mapMedia(PostRequest request, @MappingTarget Post post) {
        if (request.getMedia() != null) {
            Post.Media media = new Post.Media();
            media.setUrl(request.getMedia().getUrl());
            media.setType(request.getMedia().getType());
            post.setMedia(media);
        } else {
            post.setMedia(null);
        }
    }
}
