package kz.aitu.corporationy.service;

import kz.aitu.corporationy.dto.AuthenticatedUser;
import kz.aitu.corporationy.dto.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    void uploadImage(Long id, MultipartFile file, AuthenticatedUser authenticatedUser);

    List<ImageResponse> getImages(Long id);

    byte[] getImage(Long id);
}