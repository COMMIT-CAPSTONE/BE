package commitcapstone.commit.community.service;

import commitcapstone.commit.common.code.CommunityErrorCode;
import commitcapstone.commit.common.code.UserErrorCode;
import commitcapstone.commit.common.exception.CommunityException;
import commitcapstone.commit.common.exception.UserException;
import commitcapstone.commit.community.dto.*;
import commitcapstone.commit.community.entity.Comment;
import commitcapstone.commit.community.entity.Community;
import commitcapstone.commit.community.entity.CommunitySortType;
import commitcapstone.commit.community.entity.ReactionType;
import commitcapstone.commit.community.repository.CommentRepository;
import commitcapstone.commit.community.repository.CommunityRepository;
import commitcapstone.commit.community.repository.ReactionRepository;
import commitcapstone.commit.user.User;
import commitcapstone.commit.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;
    private final UserRepository userRepository;


    public CommunityPostResponse addCommunityPost(String email, CommunityPostRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Community community = Community.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        communityRepository.save(community);

        return CommunityPostResponse.builder()
                .id(community.getId())
                .title(request.getTitle())
                .content(request.getContent())
                .authorName(user.getName())
                .authorId(user.getId())
                .authorTier(user.getTier())
                .createdAt(LocalDateTime.now())
                .build();
    }


    public CommunityPostsResponse getCommunityPosts(int page, int size, String keyword, CommunitySortType sort) {



        Sort sortOption = switch (sort) {
            case LATEST -> Sort.by(Sort.Direction.DESC, "createdAt");
            case COMMENT -> Sort.by(Sort.Direction.DESC, "commentCount");
            case REACTION -> Sort.by(Sort.Direction.DESC, "reactionCount");
        };

        Pageable pageable = PageRequest.of(page, size, sortOption);

        Page<Community> list;
        if (keyword == "") {
            list = communityRepository.findAll(pageable);
        } else {
            list = communityRepository.findAll(pageable);
        }

        CommunityPostsResponse response = new CommunityPostsResponse();

        for (Community community : list) {
            CommunityPostBase postBase = CommunityPostBase.builder()
                    .id(community.getId())
                    .title(community.getTitle())
                    .content(community.getContent())
                    .authorName(community.getAuthor().getName())
                    .authorId(community.getAuthor().getId())
                    .authorTier(community.getAuthor().getTier())
                    .authorProfile(community.getAuthor().getProfile())
                    .commentCount(commentRepository.countByCommunityId(community.getId()))
                    .reactionCount(reactionRepository.countByCommunityId(community.getId()))
                    .createdAt(community.getCreatedAt())
                    .updatedAt(community.getUpdatedAt())
                    .build();

            response.getPosts().add(postBase);
        }

        List<Community> popularPosts = communityRepository.findTop10Popular();
        for (Community community : popularPosts) {
            CommunityPostBase postBase = CommunityPostBase.builder()
                    .id(community.getId())
                    .title(community.getTitle())
                    .content(community.getContent())
                    .authorName(community.getAuthor().getName())
                    .authorId(community.getAuthor().getId())
                    .authorTier(community.getAuthor().getTier())
                    .authorProfile(community.getAuthor().getProfile())
                    .commentCount(commentRepository.countByCommunityId(community.getId()))
                    .reactionCount(reactionRepository.countByCommunityId(community.getId()))
                    .createdAt(community.getCreatedAt())
                    .updatedAt(community.getUpdatedAt())
                    .build();

            response.getPopularPosts().add(postBase);
        }

        response.setKeyword(keyword);
        response.setSortType(sort);

        return response;

    }

    public CommunityDetailResponse getCommunityPostDetail(String email, Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new CommunityException(CommunityErrorCode.NOT_FOUND_COMMUNITY));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        CommunityDetailResponse response = CommunityDetailResponse.builder()
                .id(community.getId())
                .title(community.getTitle())
                .content(community.getContent())
                .authorName(community.getAuthor().getName())
                .authorId(community.getAuthor().getId())
                .authorTier(community.getAuthor().getTier())
                .authorProfile(community.getAuthor().getProfile())
                .createdAt(community.getCreatedAt())
                .updatedAt(community.getUpdatedAt())
                .build();


        response.setMyReaction(reactionRepository.findByCommunityIdAndUserId(id, user.getId())
                .map(reaction -> reaction.getReactionType())
                .orElse(null));

        Map<ReactionType, Integer> reactionMap = Map.of();
        List<Object[]> ReactionGroup = reactionRepository.countGroupedByType(id);
        for (Object[] obj : ReactionGroup) {
            ReactionType reactionType = (ReactionType) obj[0];
            Integer count = ((Number) obj[1]).intValue();
            reactionMap.put(reactionType, count);
        }
        response.setReaction(reactionMap);

        // 댓글 목록
        List<Comment> allComments = commentRepository.findAllByCommunityIdAndIsDeletedFalseOrderByCreatedAtAsc(id);
        Map<Long, CommunityDetailComment> commentDtoMap = new java.util.HashMap<>();
        List<CommunityDetailComment> rootComments = new ArrayList<>();

        for (Comment c : allComments) {
            CommunityDetailComment dto = CommunityDetailComment.builder()
                    .commentId(c.getId())
                    .authorName(c.getAuthor().getName())
                    .authorId(c.getAuthor().getId())
                    .authorTier(c.getAuthor().getTier())
                    .content(c.isDeleted() ? "삭제된 댓글입니다" : c.getContent())
                    .createdAt(c.getCreatedAt())
                    .replies(new ArrayList<>())
                    .build();

            commentDtoMap.put(dto.getCommentId(), dto);

            if (c.getParent() == null) {
                rootComments.add(dto);
            } else {

                CommunityDetailComment parentDto = commentDtoMap.get(c.getParent().getId());
                if (parentDto != null) {
                    parentDto.getReplies().add(dto);
                }
            }
        }

        response.setComments(rootComments);

        return response;
    }

    public void deleteCommunityPost(String email, Long id) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new CommunityException(CommunityErrorCode.NOT_FOUND_COMMUNITY));

        if (!community.getAuthor().getId().equals(user.getId())) {
            throw new CommunityException(CommunityErrorCode.UNAUTHORIZED);
        }

        communityRepository.delete(community);
    }


    public CommunityPostResponse updateCommunityPost(String email, Long id, CommunityPostRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new CommunityException(CommunityErrorCode.NOT_FOUND_COMMUNITY));

        if (!community.getAuthor().getId().equals(user.getId())) {
            throw new CommunityException(CommunityErrorCode.UNAUTHORIZED);
        }

        community.setTitle(request.getTitle());
        community.setContent(request.getContent());
        community.setUpdatedAt(LocalDateTime.now());

        communityRepository.save(community);

        return CommunityPostResponse.builder()
                .id(community.getId())
                .title(community.getTitle())
                .content(community.getContent())
                .authorName(user.getName())
                .authorId(user.getId())
                .authorTier(user.getTier())
                .createdAt(community.getCreatedAt())
                .build();
    }

    public CommunityPostsResponse getMyCommunityPosts(String email, int page, int size) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Community> communityPage = communityRepository.findByAuthorId(user.getId(), pageable);

        CommunityPostsResponse response = new CommunityPostsResponse();

        for (Community community : communityPage) {
            CommunityPostBase postBase = CommunityPostBase.builder()
                    .id(community.getId())
                    .title(community.getTitle())
                    .content(community.getContent())
                    .authorName(community.getAuthor().getName())
                    .authorId(community.getAuthor().getId())
                    .authorTier(community.getAuthor().getTier())
                    .authorProfile(community.getAuthor().getProfile())
                    .commentCount(commentRepository.countByCommunityId(community.getId()))
                    .reactionCount(reactionRepository.countByCommunityId(community.getId()))
                    .createdAt(community.getCreatedAt())
                    .updatedAt(community.getUpdatedAt())
                    .build();

            response.getPosts().add(postBase);
        }


        return response;
    }


}

