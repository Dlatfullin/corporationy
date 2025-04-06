package kz.aitu.corporationy.service.impl;

import kz.aitu.corporationy.repository.LikeRepository;
import kz.aitu.corporationy.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Override
    public int getLikeCountForPost(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}