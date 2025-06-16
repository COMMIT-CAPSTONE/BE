package commitcapstone.commit.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChallengeErrorCode implements ErrorCode {

    /*
        400 BAD_REQUEST
     */
    USER_HAVE_ONLY_ONE_CHALLENGE(HttpStatus.BAD_REQUEST, "유저는 하나의 챌린지에만 참여할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
