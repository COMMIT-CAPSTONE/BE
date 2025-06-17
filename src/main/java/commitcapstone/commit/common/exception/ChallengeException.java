package commitcapstone.commit.common.exception;

import commitcapstone.commit.common.code.ErrorCode;
import lombok.Getter;

@Getter
public class ChallengeException extends RuntimeException {
    private final ErrorCode errcode;

    public ChallengeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errcode = errorCode;
    }

}
