package commitcapstone.commit.common.exception;

import commitcapstone.commit.common.code.type.ErrorCode;
import lombok.Getter;

@Getter
public class OauthException extends RuntimeException {
    private final ErrorCode errorCode;

    public OauthException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
