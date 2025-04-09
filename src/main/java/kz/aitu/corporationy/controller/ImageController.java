package kz.aitu.corporationy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.aitu.corporationy.config.OpenApiConfig;
import kz.aitu.corporationy.dto.AuthenticatedUser;
import kz.aitu.corporationy.dto.ImageResponse;
import kz.aitu.corporationy.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Image", description = "Endpoints for upload and receive images of posts")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(
            summary = "Upload image into post by id",
            security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(path = "/{postId}/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadImage(@PathVariable("postId") Long id,
                                            @RequestParam("file") MultipartFile file,
                                            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        imageService.uploadImage(id, file, authenticatedUser);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get all images of a post",
            description = "Returns a list of images (as base64 + metadata) that belong to the specified post."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of images returned"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping(path = "/{postId}/images", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ImageResponse> getImages(@PathVariable("postId") Long id) {
        return imageService.getImages(id);
    }

    @Operation(
            summary = "Get raw image by image ID",
            description = "Returns a single image in binary format (JPEG) by its unique ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image binary returned"),
            @ApiResponse(responseCode = "404", description = "Image not found")
    })
    @GetMapping(path = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageService.getImage(id));
    }
}