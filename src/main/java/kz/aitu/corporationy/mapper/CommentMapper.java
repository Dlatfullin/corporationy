package kz.aitu.corporationy.mapper;

import kz.aitu.corporationy.dto.CommentRequest;
import kz.aitu.corporationy.dto.CommentResponse;
import kz.aitu.corporationy.entity.Comment;
import kz.aitu.corporationy.entity.Post;
import kz.aitu.corporationy.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "content", source = "request.content")
    Comment toEntity(CommentRequest request, Post post, User user);

    @Mapping(target = "author", source = "comment.user.username")
    CommentResponse toDto(Comment comment);

    List<CommentResponse> toDto(List<Comment> comments);

    void updateComment(@MappingTarget Comment comment, CommentRequest request);
}