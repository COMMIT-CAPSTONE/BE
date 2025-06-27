package commitcapstone.commit.community;

import commitcapstone.commit.community.dto.CommunityPostBase;
import commitcapstone.commit.community.dto.CommunityPostRequest;
import commitcapstone.commit.community.dto.CommunityPostsResponse;
import commitcapstone.commit.community.entity.Community;
import commitcapstone.commit.community.entity.CommunitySortType;
import commitcapstone.commit.community.repository.CommentRepository;
import commitcapstone.commit.community.repository.CommunityRepository;
import commitcapstone.commit.community.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;

    public void addCommunityPost(String email, CommunityPostRequest request) {
        Community community = Community.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .authorEmail(email)
                .build();

        communityRepository.save(community);
    }
    public CommunityPostsResponse getCommunityPosts( int page, int size, String keyword, CommunitySortType sort) {
        Sort sortOption = switch (sort) {
            case LATEST -> Sort.by(Sort.Direction.DESC, "createdAt");
            case COMMENT -> Sort.by(Sort.Direction.DESC, "commentCount");
            case REACTION -> Sort.by(Sort.Direction.DESC, "reactionCount");
        };

        Pageable pageable = PageRequest.of(page, size, sortOption);

        Page<Community> list;
        if (keyword == "") {
            list =  communityRepository.findAll(pageable);
        } else {
            list =  communityRepository.findAll(pageable);
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
                    .commentCount(commentRepository.countByCommunityId(community.getId()))
                    .reactionCount(reactionRepository.countByCommunityId(community.getId()))
                    .createdAt(community.getCreatedAt())
                    .updatedAt(community.getUpdatedAt())
                    .build();

            response.getPosts().add(postBase);
        }

        response.setKeyword(keyword);
        response.setSortType(sort);

        return response;

    }

}

