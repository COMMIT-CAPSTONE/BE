package commitcapstone.commit.community.service;

import commitcapstone.commit.common.code.CommunityErrorCode;
import commitcapstone.commit.common.code.UserErrorCode;
import commitcapstone.commit.common.exception.CommunityException;
import commitcapstone.commit.common.exception.UserException;
import commitcapstone.commit.community.dto.CommentRequest;
import commitcapstone.commit.community.dto.CommentResponse;
import commitcapstone.commit.community.entity.Comment;
import commitcapstone.commit.community.entity.Community;
import commitcapstone.commit.community.repository.CommentRepository;
import commitcapstone.commit.community.repository.CommunityRepository;
import commitcapstone.commit.community.repository.ReactionRepository;
import commitcapstone.commit.user.User;
import commitcapstone.commit.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;

    public CommentResponse addComment(String email, Long communityId, CommentRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CommunityException(CommunityErrorCode.NOT_FOUND_COMMUNITY));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(user)
                .community(community)
                .createdAt(LocalDateTime.now())
                .parent(null) // 대댓글이 아닌 경우 parent는 null
                .build();

        commentRepository.save(comment);

        CommentResponse response = new CommentResponse();
        response.setCommentId(comment.getId());
        return response;
    }

    public CommentResponse addReply(String email, Long communityId, CommentRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CommunityException(CommunityErrorCode.NOT_FOUND_COMMUNITY));

        Comment comment = commentRepository.findById(request.getParentCommentId())
                .orElseThrow(() -> new CommunityException(CommunityErrorCode.NOT_FOUND_COMMENT));

        Comment reply = Comment.builder()
                .content(request.getContent())
                .author(user)
                .community(community)
                .createdAt(LocalDateTime.now())
                .parent(comment) // 대댓글이 아닌 경우 parent는 null
                .build();

        commentRepository.save(reply);

        CommentResponse response = new CommentResponse();
        response.setCommentId(comment.getId());
        return response;

    }

}
