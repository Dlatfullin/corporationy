package kz.aitu.corporationy.mapper;

import kz.aitu.corporationy.dto.AuthenticatedUser;
import kz.aitu.corporationy.dto.RegistrationRequest;
import kz.aitu.corporationy.dto.UserResponse;
import kz.aitu.corporationy.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    AuthenticatedUser toAuthenticatedUser(User user);

    @Mapping(target = "followers", ignore = true)
    UserResponse toUserResponse(User user);

    @Mapping(target = "followers", source = "numberOfFollowers", defaultValue = "0")
    UserResponse toUserResponse(User user, int numberOfFollowers);

    @Mapping(target = "password", source = "password")
    @Mapping(target = "role", constant = "ROLE_USER")
    User toEntity(RegistrationRequest user, String password);
}