package kz.aitu.corporationy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.aitu.corporationy.dto.AuthenticatedUser;
import kz.aitu.corporationy.dto.CommentRequest;
import kz.aitu.corporationy.dto.CommentResponse;
import kz.aitu.corporationy.entity.Comment;
import kz.aitu.corporationy.entity.Post;
import kz.aitu.corporationy.entity.User;
import kz.aitu.corporationy.mapper.CommentMapper;
import kz.aitu.corporationy.repository.CommentRepository;
import kz.aitu.corporationy.repository.UserRepository;
import kz.aitu.corporationy.service.CommentService;
import kz.aitu.corporationy.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    @Override
    public List<CommentResponse> getCommentsForPost(Long postId) {
        Post post = postService.getPostById(postId);
        return commentMapper.toDto(post.getComments());
    }

    @Override
    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest request, AuthenticatedUser authenticatedUser) {
        Post post = postService.getPostById(postId);
        User user = userRepository.findByUsername(authenticatedUser.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Comment comment = commentMapper.toEntity(request, post, user);
        commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long id, CommentRequest request, AuthenticatedUser authenticatedUser) {
        Comment comment = getCommentById(id);
        validatePermission(comment, authenticatedUser);
        commentMapper.updateComment(comment, request);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteComment(Long id, AuthenticatedUser aUser) {
        Comment comment = getCommentById(id);
        validatePermission(comment, aUser);
        commentRepository.delete(comment);
    }

    private void validatePermission(Comment comment, AuthenticatedUser authenticatedUser) {
        boolean isAdmin = authenticatedUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = comment.getUser().getId().equals(authenticatedUser.getId());
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You do not have permission to modify this post.");
        }
    }

    private Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(id)));
    }
}