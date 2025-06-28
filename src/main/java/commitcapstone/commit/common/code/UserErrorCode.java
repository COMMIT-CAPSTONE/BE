package commitcapstone.commit.common.code;

import commitcapstone.commit.common.code.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    // 400 BAD_REQUEST
    DUPLICATED_USER_NAME(HttpStatus.BAD_REQUEST, "중복된 유저 이름입니다."),
    INVALID_USER_NAME(HttpStatus.BAD_REQUEST, "유효하지 않은 유저 이름입니다."),
    SIZE_OVER_USER_NAME(HttpStatus.BAD_REQUEST, "유저 이름은 2자 이상 20자 이하여야 합니다."),

    // 404 NOT_FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}