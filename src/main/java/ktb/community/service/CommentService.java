package ktb.community.service;

import jakarta.transaction.Transactional;
import ktb.community.dto.comment.request.CreateCommentRequest;
import ktb.community.dto.comment.request.UpdateCommentRequest;
import ktb.community.dto.comment.response.CreateCommentResponse;
import ktb.community.dto.comment.response.GetCommentsResponse;
import ktb.community.dto.comment.response.GetCommentsResponse.CommentSummary;
import ktb.community.dto.comment.response.UpdateCommentResponse;
import ktb.community.dto.post.response.GetPostsResponse.PostSummary.Author;
import ktb.community.entity.Comment;
import ktb.community.entity.Post;
import ktb.community.entity.User;
import ktb.community.exception.CustomException;
import ktb.community.exception.ErrorCode;
import ktb.community.repository.CommentRepository;
import ktb.community.repository.PostRepository;
import ktb.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CreateCommentResponse createComment(Long userId, Long postId, CreateCommentRequest req) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_FOUND));

        post.increaseCommentCount();

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(req.content())
                .build();

        Comment saved = commentRepository.save(comment);

        return new CreateCommentResponse(post.getId(), saved.getId());
    }

    @Transactional
    public GetCommentsResponse getComments(Long userId, Long postId, Long cursor, int size) {
        List<Comment> comments = commentRepository.findCommentsByCursor(postId, cursor, size + 1);

        boolean hasNext = comments.size() > size;
        if (hasNext) {
            comments = comments.subList(0, size);
        }
        Long nextCursor = hasNext ? comments.get(comments.size() - 1).getId() : null;

        List<CommentSummary> summaries = comments.stream()
                .map(c -> new CommentSummary(
                        c.getId(),
                        c.getContent(),
                        c.getCreatedAt(),
                        c.getUser().getId() == userId,
                        new CommentSummary.Author(
                                c.getUser().getNickname(),
                                c.getUser().getProfileImageUrl()
                        )
                )).toList();

        return new GetCommentsResponse(summaries, nextCursor, hasNext);
    }

    @Transactional
    public UpdateCommentResponse updateComment(Long userId, Long postId, Long commentId, UpdateCommentRequest req) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (comment.getUser().getId() != userId) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        comment.updateComment(req.content());

        return new UpdateCommentResponse(comment.getId());
    }

    @Transactional
    public void deleteComment(Long userId, Long postId, Long commentId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (comment.getUser().getId() != userId) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        commentRepository.delete(comment);
    }
}
