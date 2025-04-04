package kz.aitu.corporationy.service;

import kz.aitu.corporationy.dto.*;

public interface AuthService {

    UserResponse registration(RegistrationRequest request);

    GeneratedToken login(LoginRequest loginRequest);

    UserResponse getAuthenticatedUser(AuthenticatedUser user);
}