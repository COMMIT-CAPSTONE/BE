    package commitcapstone.commit.community.controller;

    import commitcapstone.commit.common.response.SuccessResponse;
    import commitcapstone.commit.community.CommunityService;
    import commitcapstone.commit.community.dto.CommunityPostRequest;
    import commitcapstone.commit.community.dto.CommunityPostsResponse;
    import commitcapstone.commit.community.entity.CommunitySortType;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Page;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/community")
    @RequiredArgsConstructor
    public class CommunityController {

        private final CommunityService communityService;

        @PostMapping("/post")
        public ResponseEntity<SuccessResponse<Page>> addCommunityPosts(@AuthenticationPrincipal String email,
                                                                       @RequestBody CommunityPostRequest request) {

            return ResponseEntity.ok(new SuccessResponse<>("커뮤니티 게시글 작성 성공", response));

        }
        @GetMapping("/post")
        public ResponseEntity<SuccessResponse<CommunityPostsResponse>> getCommunityPosts(@RequestParam(defaultValue = "") String keyWord,
                                                                                         @RequestParam(defaultValue = "LATEST") CommunitySortType sort,
                                                                                         @RequestParam(defaultValue = "0") int page,
                                                                                         @RequestParam(defaultValue = "10") int size) {
            CommunityPostsResponse response = communityService.getCommunityPosts(page, size, keyWord, sort);

            return ResponseEntity.ok(new SuccessResponse<>("커뮤니티 게시글 조회 성공", response));

        }
    }
