package kz.aitu.corporationy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.aitu.corporationy.dto.AuthenticatedUser;
import kz.aitu.corporationy.dto.UserResponse;
import kz.aitu.corporationy.entity.Token;
import kz.aitu.corporationy.entity.User;
import kz.aitu.corporationy.mapper.UserMapper;
import kz.aitu.corporationy.repository.TokenRepository;
import kz.aitu.corporationy.repository.UserRepository;
import kz.aitu.corporationy.service.SubscriptionService;
import kz.aitu.corporationy.service.UserService;
import kz.aitu.corporationy.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final SubscriptionService subscriptionService;
    private final UserMapper userMapper;

    @Override
    public List<UserResponse> getUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.stream()
                .map(user -> userMapper.toUserResponse(user, subscriptionService.getCountFollowers(user.getId())))
                .toList();
    }

    @Override
    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id %s was not found".formatted(id)));
        return userMapper.toUserResponse(user, subscriptionService.getCountFollowers(user.getId()));
    }

    @Override
    public AuthenticatedUser getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username %s was not found".formatted(username)));
        return userMapper.toAuthenticatedUser(user);
    }

    @Override
    public Optional<AuthenticatedUser> getByToken(String token) {
        var hashedToken = TokenUtils.hash(token);
        return tokenRepository.findByHashAndExpiredAtAfter(hashedToken, LocalDateTime.now())
                .map(Token::getUser)
                .map(userMapper::toAuthenticatedUser);
    }
}