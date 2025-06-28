package commitcapstone.commit.common.code;

import commitcapstone.commit.common.code.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExerErrorCode implements ErrorCode {


    INVALID_TIME_TYPE(HttpStatus.BAD_REQUEST, "시간 타입이 잘못되었습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
