package commitcapstone.commit.common.code;

import commitcapstone.commit.common.code.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommunityErrorCode implements ErrorCode {

    /*
        400 BAD_REQUEST
     */

    /*
    *
    * */
    NOT_FOUND_COMMUNITY(HttpStatus.BAD_REQUEST, "존재하지 않는 커뮤니티 게시글입니다."),
    NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST, "존재하지 않는 커뮤니티 댓글입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "수정 및 삭제 권한이 없는 사용자입니다."),
    DELETED_POST(HttpStatus.BAD_REQUEST, "이미 삭제된 게시글입니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
