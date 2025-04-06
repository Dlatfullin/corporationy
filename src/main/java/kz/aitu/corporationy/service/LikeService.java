package kz.aitu.corporationy.service;

import kz.aitu.corporationy.dto.AuthenticatedUser;
import kz.aitu.corporationy.dto.LikeStatusResponse;

public interface LikeService {

    void toggleLike(Long postId, AuthenticatedUser user);

    LikeStatusResponse hasUserLiked(Long postId, AuthenticatedUser user);
}