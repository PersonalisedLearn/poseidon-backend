package com.personalisedlearn.poseidon.mapper;

import com.personalisedlearn.poseidon.dto.UserRequest;
import com.personalisedlearn.poseidon.dto.UserResponse;
import com.personalisedlearn.poseidon.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<UserRequest, User> {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Override
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "followers", constant = "0")
    @Mapping(target = "following", constant = "0")
    User toEntity(UserRequest dto);

    @Override
    UserRequest toDto(User entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "avatar", source = "avatar")
    @Mapping(target = "bio", source = "bio")
    @Mapping(target = "followers", source = "followers")
    @Mapping(target = "following", source = "following")
    @Mapping(target = "gender", source = "gender")
    UserResponse toResponse(User user);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "following", ignore = true)
    @Mapping(target = "gender", source = "gender")
    void updateEntityFromDto(UserRequest dto, @MappingTarget User entity);
}
