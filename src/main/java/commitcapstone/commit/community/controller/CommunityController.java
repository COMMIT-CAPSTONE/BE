package commitcapstone.commit.community.controller;

import commitcapstone.commit.common.response.SuccessResponse;
import commitcapstone.commit.community.dto.*;
import commitcapstone.commit.community.service.CommentService;
import commitcapstone.commit.community.service.CommunityService;
import commitcapstone.commit.community.entity.CommunitySortType;
import commitcapstone.commit.community.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;
    private final CommentService commentService;
    private final ReactionService reactionService;

    @PostMapping("/post")
    public ResponseEntity<SuccessResponse<CommunityPostResponse>> addCommunityPost(@AuthenticationPrincipal String email,
                                                                                   @RequestBody CommunityPostRequest request) {
        CommunityPostResponse response = communityService.addCommunityPost(email, request);
        return ResponseEntity.ok(new SuccessResponse<>("커뮤니티 게시글 작성 성공", response));
    }


    @GetMapping("/post")
    public ResponseEntity<SuccessResponse<CommunityPostsResponse>> getCommunityPosts(@RequestParam(defaultValue = "") String keyword,
                                                                                     @RequestParam(defaultValue = "LATEST") CommunitySortType sort, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        CommunityPostsResponse response = communityService.getCommunityPosts(page, size, keyWord, sort);

        return ResponseEntity.ok(new SuccessResponse<>("커뮤니티 게시글 조회 성공", response));

    }


    @PostMapping("/comment/{id}")
    public ResponseEntity<SuccessResponse<Void>> addComment(@AuthenticationPrincipal String email,
                                                            @PathVariable Long id,
                                                            @RequestBody CommentRequest request) {
        if (request.getParentCommentId() == null) {
            commentService.addComment(email, id, request);
            return ResponseEntity.ok(new SuccessResponse<>("댓글 작성 성공", null));
        } else {
            commentService.addReply(email, id, request);
            return ResponseEntity.ok(new SuccessResponse<>("대댓글 작성 성공", null));
        }
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<SuccessResponse<Void>> deleteComment(@AuthenticationPrincipal String email,
                                                               @PathVariable Long id,
                                                               @RequestBody CommentRequest request) {
        commentService.updateComment(email, id, request);
        return ResponseEntity.ok(new SuccessResponse<>("댓글 삭제 성공", null));
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<SuccessResponse<Void>> deleteReply(@AuthenticationPrincipal String email,
                                                              @PathVariable Long id) {
        commentService.deleteComment(email, id);
        return ResponseEntity.ok(new SuccessResponse<>("대댓글 삭제 성공", null));
    }


    @PostMapping("/reaction/{id}")
    public ResponseEntity<SuccessResponse<Void>> addReaction(@AuthenticationPrincipal String email,
                                                             @PathVariable Long id
                                                             ,@RequestBody ReactionRequest request) {
        String msg = reactionService.addReaction(email, id, request);
        return ResponseEntity.ok(new SuccessResponse<>(msg, null));
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<SuccessResponse<CommunityDetailResponse>> getCommunityPostDetauil(@AuthenticationPrincipal String email,
                                                                                           @PathVariable Long id) {
        CommunityDetailResponse response = communityService.getCommunityPostDetail(email, id);

        return ResponseEntity.ok(new SuccessResponse<>("커뮤니티 게시글 상세 조회 성공", response));
    }


    @DeleteMapping("/post/{id}")
    public ResponseEntity<SuccessResponse<Void>> deleteCommunityPost(@AuthenticationPrincipal String email,
                                                                     @PathVariable Long id) {
        communityService.deleteCommunityPost(email, id);
        return ResponseEntity.ok(new SuccessResponse<>("커뮤니티 게시글 삭제 성공", null));
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<SuccessResponse<CommunityPostResponse>> updateCommunityPost(@AuthenticationPrincipal String email,
                                                                                      @PathVariable Long id,
                                                                                      @RequestBody CommunityPostRequest request) {
        CommunityPostResponse response = communityService.updateCommunityPost(email, id, request);
        return ResponseEntity.ok(new SuccessResponse<>("커뮤니티 게시글 수정 성공", response));
    }

    @GetMapping("/post/my")
    public ResponseEntity<SuccessResponse<CommunityPostsResponse>> getMyCommunityPosts(@AuthenticationPrincipal String email,
                                                                                       @RequestParam(defaultValue = "0") int page,
                                                                                       @RequestParam(defaultValue = "10") int size) {
        CommunityPostsResponse response = communityService.getMyCommunityPosts(email, page, size);
        return ResponseEntity.ok(new SuccessResponse<>("내 커뮤니티 게시글 조회 성공", response));
    }


}
