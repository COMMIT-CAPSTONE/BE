package commitcapstone.commit.common.exception;

import commitcapstone.commit.common.code.type.ErrorCode;
import lombok.Getter;

@Getter
public class CommunityException extends RuntimeException {
    private final ErrorCode errorCode;

    public CommunityException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;

    }

}
