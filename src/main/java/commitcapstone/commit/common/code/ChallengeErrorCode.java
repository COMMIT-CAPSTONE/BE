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
    USER_HAVE_ONLY_ONE_CHALLENGE(HttpStatus.BAD_REQUEST, "유저는 하나의 챌린지에만 참여할 수 있습니다."),
    INSUFFICIENT_USER_POINTS(HttpStatus.BAD_REQUEST, "사용자의 보유 포인트가 베팅 포인트보다 적습니다."),
    INVALID_USER_POINT(HttpStatus.BAD_REQUEST, "사용자 베팅 포인트는 음수일 수 없습니다."),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "시작일은 종료일보다 앞서야 합니다."),
    INVALID_CHALLENGE_PERIOD(HttpStatus.BAD_REQUEST, "챌린지 기간은 최소 3일 이상이어야 합니다."),
    INVALID_DAILY_GOAL_TIME(HttpStatus.BAD_REQUEST, "DAILY 챌린지의 목표 시간은 1시간 이상 24시간 이하로 설정해야 합니다."),
    INVALID_TOTAL_GOAL_TIME(HttpStatus.BAD_REQUEST, "TOTAL 챌린지의 목표 시간은 (일수 x 1시간) 이상으로 설정해야 합니다."),
    ALREAY_START_CHALLENGE(HttpStatus.BAD_REQUEST, "이미 시작한 챌린지는 참여할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
