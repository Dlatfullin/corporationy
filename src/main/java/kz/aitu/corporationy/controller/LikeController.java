package kz.aitu.corporationy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import kz.aitu.corporationy.config.OpenApiConfig;
import kz.aitu.corporationy.dto.AuthenticatedUser;
import kz.aitu.corporationy.dto.LikeStatusResponse;
import kz.aitu.corporationy.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like", description = "Endpoints for liking and checking like status of posts")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @Operation(
            summary = "Toggle like for a post",
            description = "Allows an authenticated user to like or unlike a post. If already liked, it will be unliked and vice versa.",
            security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like status successfully toggled"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PostMapping("/{postId}/like")
    public void toggleLike(@PathVariable @Positive Long postId,
                           @AuthenticationPrincipal AuthenticatedUser user) {
        likeService.toggleLike(postId, user);
    }

    @Operation(
            summary = "Check if current user liked a post",
            description = "Returns whether the currently authenticated user has liked the specified post.",
            security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully returned like status",
                    content = @Content(schema = @Schema(implementation = LikeStatusResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping(path = "/{postId}/liked", produces = MediaType.APPLICATION_JSON_VALUE)
    public LikeStatusResponse hasUserLiked(@PathVariable @Positive Long postId,
                                           @AuthenticationPrincipal AuthenticatedUser user) {
        return likeService.hasUserLiked(postId, user);
    }
}