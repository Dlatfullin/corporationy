package kz.aitu.corporationy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import kz.aitu.corporationy.dto.PostRequest;
import kz.aitu.corporationy.dto.PostResponse;
import kz.aitu.corporationy.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post", description = "Operations related to posts")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(
            summary = "Create a new post",
            description = "Allows an authenticated user to create a new post",
            security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Post successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or validation error"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - authentication required"
            )
    })
    @PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> createPost(@RequestBody @Valid PostRequest postRequest,
                                                   @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        var post = postService.create(postRequest, authenticatedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @Operation(summary = "Get a list of posts", description = "Retrieves a list of posts.")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PageableAsQueryParam
    public List<PostResponse> getPosts(@Parameter(hidden = true) Pageable pageable) {
        return postService.getPosts(pageable);
    }

    @Operation(
            summary = "Get a post by ID",
            description = "Retrieves a post by its unique ID. The user must be authenticated.",
            security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Post successfully retrieved",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid ID supplied"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - authentication required"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Post not found"
            )
    })
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostResponse getPost(@PathVariable @Positive Long id) {
        return postService.getPost(id);
    }

    @Operation(
            summary = "Update an existing post",
            description = "Updates a post by ID. Requires authentication and ownership of the post.",
            security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Post successfully updated",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or validation error"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - only the author can update the post"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Post not found"
            )
    })
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PostResponse updatePost(@PathVariable @Positive Long id,
                                   @RequestBody @Valid PostRequest postRequest,
                                   @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return postService.updatePost(id, postRequest, authenticatedUser);
    }

    @Operation(
            summary = "Delete a post",
            description = "Deletes a post by ID. Only the post owner or admin can perform this action.",
            security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Post successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - only the author or admin can delete the post"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Post not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable @Positive Long id,
                                           @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        postService.deletePost(id, authenticatedUser);
        return ResponseEntity.noContent().build();
    }
}