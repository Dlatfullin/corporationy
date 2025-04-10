package kz.aitu.corporationy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.aitu.corporationy.dto.AuthenticatedUser;
import kz.aitu.corporationy.dto.UserResponse;
import kz.aitu.corporationy.entity.Subscription;
import kz.aitu.corporationy.entity.User;
import kz.aitu.corporationy.mapper.UserMapper;
import kz.aitu.corporationy.repository.SubscriptionRepository;
import kz.aitu.corporationy.repository.UserRepository;
import kz.aitu.corporationy.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void toggleSubscription(Long targetId, AuthenticatedUser currentUser) {
        if (currentUser.getId().equals(targetId)) {
            throw new IllegalArgumentException("You can't subscribe to yourself");
        }
        subscriptionRepository.findBySubscriberIdAndTargetId(currentUser.getId(), targetId)
                .ifPresentOrElse(subscriptionRepository::delete, () -> {
                            User subscriber = userRepository.findByUsername(currentUser.getUsername())
                                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
                            User targetUser = userRepository.findById(targetId)
                                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
                            Subscription subscription = Subscription.builder()
                                    .subscriber(subscriber)
                                    .target(targetUser)
                                    .build();
                            subscriptionRepository.save(subscription);
                        });
    }

    @Override
    public boolean isSubscribed(Long targetId, AuthenticatedUser currentUser) {
        return subscriptionRepository.existsBySubscriberIdAndTargetId(currentUser.getId(), targetId);
    }

    @Override
    public List<UserResponse> getSubscribers(Long userId) {
        List<Subscription> subscribers = subscriptionRepository.findByTargetId(userId);
        return subscribers.stream()
                .map(sub -> userMapper.toUserResponse(sub.getSubscriber(),
                        getCountFollowers(sub.getSubscriber().getId())))
                .toList();
    }

    @Override
    public List<UserResponse> getSubscriptions(Long userId) {
        List<Subscription> subscriptions = subscriptionRepository.findBySubscriberId(userId);
        return subscriptions.stream()
                .map(sub -> userMapper.toUserResponse(sub.getTarget(),
                        getCountFollowers(sub.getTarget().getId())))
                .toList();
    }

    @Override
    public int getCountFollowers(Long userId) {
        return subscriptionRepository.countByTargetId(userId);
    }
}