package kz.aitu.corporationy.service;

import kz.aitu.corporationy.dto.AuthenticatedUser;

import java.util.Optional;

public interface UserService {

    AuthenticatedUser getByUsername(String username);

    Optional<AuthenticatedUser> getByToken(String token);
}