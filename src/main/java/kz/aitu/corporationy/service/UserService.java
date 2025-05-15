package kz.aitu.corporationy.service;

import kz.aitu.corporationy.dto.AuthenticatedUser;
import kz.aitu.corporationy.dto.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserResponse> getUsers(Pageable pageable);

    UserResponse getUser(Long id);

    AuthenticatedUser getByUsername(String username);

    Optional<AuthenticatedUser> getByToken(String token);
}