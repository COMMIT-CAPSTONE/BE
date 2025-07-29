package commitcapstone.commit.community.dto;

import commitcapstone.commit.community.entity.CommunitySortType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommunityPostsResponse {

    private String keyword;
    private CommunitySortType sortType;

    List<CommunityPostBase> posts = new ArrayList<>();
    List<CommunityPostBase> popularPosts = new ArrayList<>();
}
