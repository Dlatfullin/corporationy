package kz.aitu.corporationy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.aitu.corporationy.dto.*;
import kz.aitu.corporationy.entity.User;
import kz.aitu.corporationy.exception.AlreadyExistsException;
import kz.aitu.corporationy.mapper.UserMapper;
import kz.aitu.corporationy.repository.UserRepository;
import kz.aitu.corporationy.service.AuthService;
import kz.aitu.corporationy.service.SubscriptionService;
import kz.aitu.corporationy.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubscriptionService subscriptionService;

    @Override
    public UserResponse registration(RegistrationRequest request) {
        if (userRepository.existsByUsernameIgnoreCase(request.username())) {
            throw new AlreadyExistsException("Username is already taken by another user");
        }
        final String encodedPassword = passwordEncoder.encode(request.password());
        final User user = userRepository.save(userMapper.toEntity(request, encodedPassword));
        return userMapper.toUserResponse(user);
    }

    @Override
    public GeneratedToken login(LoginRequest loginRequest) {
        final String username = loginRequest.username();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, loginRequest.password()));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return tokenService.generate(username);
    }

    @Override
    public UserResponse getAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        User user = userRepository.findByUsername(authenticatedUser.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        int numberOfFollowers = subscriptionService.getCountFollowers(user.getId());
        return userMapper.toUserResponse(user, numberOfFollowers);
    }
}