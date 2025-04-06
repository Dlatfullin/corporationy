package kz.aitu.corporationy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import kz.aitu.corporationy.config.OpenApiConfig;
import kz.aitu.corporationy.dto.AuthenticatedUser;
import kz.aitu.corporationy.dto.CommentRequest;
import kz.aitu.corporationy.dto.CommentResponse;
import kz.aitu.corporationy.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comments", description = "Endpoints for creating, updating, deleting, and retrieving comments")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "Get all comments for a post",
            description = "Returns a list of comments associated with a specific post ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved comments",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping(path = "/posts/{postId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CommentResponse> getComments(@PathVariable @Positive Long postId) {
        return commentService.getCommentsForPost(postId);
    }

    @Operation(
            summary = "Create a new comment",
            description = "Allows an authenticated user to create a comment under a specific post.",
            security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment successfully created",
                    content = @Content(schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping(path = "/posts/{postId}/comments", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long postId,
                                                         @RequestBody @Valid CommentRequest request,
                                                         @AuthenticationPrincipal AuthenticatedUser user) {
        var comment = commentService.createComment(postId, request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @Operation(
            summary = "Update a comment",
            description = "Updates an existing comment. Only the author or an admin can perform this operation.",
            security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment successfully updated",
                    content = @Content(schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - only author or admin can update"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @PutMapping(path = "/comments/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long id,
                                                         @RequestBody @Valid CommentRequest request,
                                                         @AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(commentService.updateComment(id, request, user));
    }

    @Operation(
            summary = "Delete a comment",
            description = "Deletes a comment by its ID. Only the author or admin can delete.",
            security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment successfully deleted"),
            @ApiResponse(responseCode = "403", description = "Forbidden - only author or admin can delete"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id,
                                              @AuthenticationPrincipal AuthenticatedUser user) {
        commentService.deleteComment(id, user);
        return ResponseEntity.noContent().build();
    }
}