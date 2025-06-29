package commitcapstone.commit.community.service;

import commitcapstone.commit.challenge.entity.Challenge;
import commitcapstone.commit.common.code.CommunityErrorCode;
import commitcapstone.commit.common.code.UserErrorCode;
import commitcapstone.commit.common.exception.CommunityException;
import commitcapstone.commit.common.exception.UserException;
import commitcapstone.commit.community.dto.ReactionRequest;
import commitcapstone.commit.community.entity.Community;
import commitcapstone.commit.community.entity.Reaction;
import commitcapstone.commit.community.entity.ReactionType;
import commitcapstone.commit.community.repository.CommunityRepository;
import commitcapstone.commit.community.repository.ReactionRepository;
import commitcapstone.commit.user.User;
import commitcapstone.commit.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;

    public String addReaction(String email, Long postId, ReactionRequest reactionRequest) {

        Community community = communityRepository.findById(postId)
                .orElseThrow(() -> new CommunityException(CommunityErrorCode.NOT_FOUND_COMMUNITY));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Reaction existingReaction = reactionRepository.findByCommunityIdAndUserId(postId, user.getId())
                .orElse(null);

        if (existingReaction == null) {            // 리액션이 없으면 새로 생성
            Reaction newReaction = Reaction.builder()
                    .targetPost(community)
                    .user(user)
                    .reactionType(reactionRequest.getReactionType())
                    .build();

            reactionRepository.save(newReaction);

            return "리액션 추가 성공" + reactionRequest.getReactionType().name();
        } else if (existingReaction.getReactionType() != reactionRequest.getReactionType()) {             // 다른 타입으로 변경한 경우 → 업데이트
            existingReaction.setReactionType(reactionRequest.getReactionType());

            community.setCommentCount(community.getReactionCount() + 1);
            reactionRepository.save(existingReaction);

            return "리액션 변경 성공" + reactionRequest.getReactionType().name();
        } else {            // 같은 타입이면 삭제

            community.setCommentCount(community.getReactionCount() - 1);
            reactionRepository.delete(existingReaction);

            return "리액션 삭제 성공" + reactionRequest.getReactionType().name();
        }
    }
}
