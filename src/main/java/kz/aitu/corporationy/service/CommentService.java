package kz.aitu.corporationy.service;

import kz.aitu.corporationy.dto.AuthenticatedUser;
import kz.aitu.corporationy.dto.CommentRequest;
import kz.aitu.corporationy.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    List<CommentResponse> getCommentsForPost(Long postId);

    CommentResponse createComment(Long postId, CommentRequest request, AuthenticatedUser authenticatedUser);

    CommentResponse updateComment(Long id, CommentRequest request, AuthenticatedUser authenticatedUser);

    void deleteComment(Long id, AuthenticatedUser aUser);
}