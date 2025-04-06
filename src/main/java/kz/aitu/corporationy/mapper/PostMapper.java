package kz.aitu.corporationy.mapper;

import kz.aitu.corporationy.dto.PostRequest;
import kz.aitu.corporationy.dto.PostResponse;
import kz.aitu.corporationy.entity.Post;
import kz.aitu.corporationy.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    Post toEntity(PostRequest postRequest, User user);

    @Mapping(target = "author", source = "username")
    @Mapping(target = "likes", constant = "0")
    PostResponse toPostResponse(Post post, String username);

    @Mapping(target = "author", source = "post.user.username")
    @Mapping(target = "likes", source = "likes")
    PostResponse toPostResponse(Post post, int likes);

    void updatePost(@MappingTarget Post post, PostRequest postRequest);
}