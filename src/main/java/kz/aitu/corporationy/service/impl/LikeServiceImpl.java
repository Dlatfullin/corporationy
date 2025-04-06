package kz.aitu.corporationy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.aitu.corporationy.dto.AuthenticatedUser;
import kz.aitu.corporationy.dto.LikeStatusResponse;
import kz.aitu.corporationy.entity.Like;
import kz.aitu.corporationy.entity.Post;
import kz.aitu.corporationy.entity.User;
import kz.aitu.corporationy.mapper.LikeMapper;
import kz.aitu.corporationy.repository.LikeRepository;
import kz.aitu.corporationy.repository.UserRepository;
import kz.aitu.corporationy.service.LikeService;
import kz.aitu.corporationy.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostService postService;
    private final UserRepository userRepository;
    private final LikeMapper likeMapper;

    @Override
    @Transactional
    public void toggleLike(Long postId, AuthenticatedUser authenticatedUser) {
        Optional<Like> like = likeRepository.findByPostIdAndUserId(postId, authenticatedUser.getId());
        if (like.isPresent()) {
            likeRepository.delete(like.get());
        } else {
            Post post = postService.getPostById(postId);
            User user = userRepository.findByUsername(authenticatedUser.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            likeRepository.save(likeMapper.toEntity(user, post));
        }
    }

    @Override
    public LikeStatusResponse hasUserLiked(Long postId, AuthenticatedUser user) {
        return new LikeStatusResponse(likeRepository.existsByPostIdAndUserId(postId, user.getId()));
    }
}