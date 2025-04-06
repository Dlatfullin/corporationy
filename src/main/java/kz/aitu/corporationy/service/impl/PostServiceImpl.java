package kz.aitu.corporationy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.aitu.corporationy.dto.AuthenticatedUser;
import kz.aitu.corporationy.dto.PostRequest;
import kz.aitu.corporationy.dto.PostResponse;
import kz.aitu.corporationy.entity.Post;
import kz.aitu.corporationy.entity.User;
import kz.aitu.corporationy.mapper.PostMapper;
import kz.aitu.corporationy.repository.PostRepository;
import kz.aitu.corporationy.repository.UserRepository;
import kz.aitu.corporationy.service.LikeService;
import kz.aitu.corporationy.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final LikeService likeService;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PostResponse create(PostRequest request, AuthenticatedUser authenticatedUser) {
        User user = userRepository.findByUsername(authenticatedUser.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Post post = postMapper.toEntity(request, user);
        post = postRepository.save(post);
        return postMapper.toPostResponse(post, authenticatedUser.getUsername());
    }

    @Override
    public PostResponse getPost(Long id) {
        Post post = getPostById(id);
        return postMapper.toPostResponse(post, likeService.getLikeCountForPost(id));
    }

    @Override
    public List<PostResponse> getPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.stream()
                .map(post -> postMapper.toPostResponse(post, likeService.getLikeCountForPost(post.getId())))
                .toList();
    }

    @Override
    @Transactional
    public PostResponse updatePost(Long id, PostRequest request, AuthenticatedUser authenticatedUser) {
        Post post = getPostById(id);
        validatePermission(post, authenticatedUser);
        postMapper.updatePost(post, request);
        return postMapper.toPostResponse(post, likeService.getLikeCountForPost(post.getId()));
    }

    @Override
    @Transactional
    public void deletePost(Long id, AuthenticatedUser authenticatedUser) {
        Post post = getPostById(id);
        validatePermission(post, authenticatedUser);
        postRepository.deleteById(id);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post with id %s not found".formatted(id)));
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