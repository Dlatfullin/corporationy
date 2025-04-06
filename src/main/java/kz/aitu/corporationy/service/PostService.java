package kz.aitu.corporationy.service;

import kz.aitu.corporationy.dto.AuthenticatedUser;
import kz.aitu.corporationy.dto.PostRequest;
import kz.aitu.corporationy.dto.PostResponse;
import kz.aitu.corporationy.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PostResponse create(PostRequest request, AuthenticatedUser user);

    PostResponse getPost(Long id);

    List<PostResponse> getPosts(Pageable pageable);

    PostResponse updatePost(Long id, PostRequest request, AuthenticatedUser authenticatedUser);

    void deletePost(Long id, AuthenticatedUser authenticatedUser);

    Post getPostById(Long id);
}