package commitcapstone.commit.common.exception;

import commitcapstone.commit.common.code.ErrorCode;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final ErrorCode errorCode;

    public UserException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
