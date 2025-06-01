package commitcapstone.commit.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ErrorResponse{
    private final boolean success;
    private final String message;

    public ErrorResponse(String message) {
        this.success = false;
        this.message = message;
    }
}
