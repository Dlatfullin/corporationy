package kz.aitu.corporationy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.aitu.corporationy.config.OpenApiConfig;
import kz.aitu.corporationy.dto.AuthenticatedUser;
import kz.aitu.corporationy.dto.UserResponse;
import kz.aitu.corporationy.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Subscription", description = "Manage user subscriptions and followers")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;


    @Operation(
            summary = "Subscribe or unsubscribe to a user",
            security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully toggled subscription"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Target user not found")
    })
    @PostMapping("/{targetId}/subscribe")
    public void toggleSubscription(@PathVariable Long targetId,
                                   @AuthenticationPrincipal AuthenticatedUser user) {
        subscriptionService.toggleSubscription(targetId, user);
    }

    @Operation(
            summary = "Get list of user's followers",
            security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved subscribers"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}/subscribers")
    public List<UserResponse> getSubscribers(@PathVariable Long userId) {
        return subscriptionService.getSubscribers(userId);
    }

    @Operation(
            summary = "Get list of users the user is subscribed to",
            security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved subscriptions"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}/subscriptions")
    public List<UserResponse> getSubscriptions(@PathVariable Long userId) {
        return subscriptionService.getSubscriptions(userId);
    }

    @Operation(
            summary = "Check if the current user is subscribed to the target user",
            security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully checked subscription status"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Target user not found")
    })
    @GetMapping("/{targetId}/subscribed")
    public boolean isSubscribed(@PathVariable Long targetId,
                                @AuthenticationPrincipal AuthenticatedUser user) {
        return subscriptionService.isSubscribed(targetId, user);
    }
}