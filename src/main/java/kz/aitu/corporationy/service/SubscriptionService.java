package kz.aitu.corporationy.service;

import kz.aitu.corporationy.dto.AuthenticatedUser;
import kz.aitu.corporationy.dto.UserResponse;

import java.util.List;

public interface SubscriptionService {

    void toggleSubscription(Long targetId, AuthenticatedUser currentUser);

    boolean isSubscribed(Long targetId, AuthenticatedUser currentUser);

    List<UserResponse> getSubscribers(Long userId);

    List<UserResponse> getSubscriptions(Long userId);

    int getCountFollowers(Long userId);
}