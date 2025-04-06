package kz.aitu.corporationy.mapper;

import kz.aitu.corporationy.entity.Like;
import kz.aitu.corporationy.entity.Post;
import kz.aitu.corporationy.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LikeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "post", source = "post")
    Like toEntity(User user, Post post);
}