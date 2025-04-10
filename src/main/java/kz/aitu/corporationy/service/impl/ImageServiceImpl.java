package kz.aitu.corporationy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.aitu.corporationy.dto.AuthenticatedUser;
import kz.aitu.corporationy.dto.ImageResponse;
import kz.aitu.corporationy.entity.Image;
import kz.aitu.corporationy.entity.Post;
import kz.aitu.corporationy.exception.ImageUploadException;
import kz.aitu.corporationy.mapper.ImageMapper;
import kz.aitu.corporationy.repository.ImageRepository;
import kz.aitu.corporationy.service.ImageService;
import kz.aitu.corporationy.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final PostService postService;
    private final ImageMapper imageMapper;

    @Override
    public void uploadImage(Long id, MultipartFile file, AuthenticatedUser authenticatedUser) {
        try {
            Post post = postService.getPostById(id);
            validatePermission(post, authenticatedUser);
            Image image = new Image();
            String imageBase64 = Base64.getEncoder().encodeToString(file.getBytes());
            image.setBase64(imageBase64);
            image.setPost(post);
            imageRepository.save(image);
        } catch (IOException e) {
            throw new ImageUploadException("Image upload failed", e);
        }
    }

    @Override
    public List<ImageResponse> getImages(Long id) {
        Post post = postService.getPostById(id);
        return imageMapper.toDto(post.getImages());
    }

    @Override
    public byte[] getImage(Long id) {
        return imageRepository.findById(id)
                .map(Image::getBase64)
                .map(Base64.getDecoder()::decode)
                .orElseThrow(() -> new EntityNotFoundException("Image not found"));
    }

    private void validatePermission(Post post, AuthenticatedUser authenticatedUser) {
        boolean isAdmin = authenticatedUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = post.getUser().getId().equals(authenticatedUser.getId());
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You do not have permission to modify this post.");
        }
    }
}