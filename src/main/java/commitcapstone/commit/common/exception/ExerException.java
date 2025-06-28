package commitcapstone.commit.common.exception;

import commitcapstone.commit.common.code.type.ErrorCode;
import lombok.Getter;

@Getter
public class ExerException extends RuntimeException {
    private final ErrorCode errorCode;

    public ExerException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
