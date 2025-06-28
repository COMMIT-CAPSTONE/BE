package commitcapstone.commit.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    @Schema(description = "리프레시 토큰", example = "eyJraWQiOiJrZ3d6b2V5c2VjcmV0In0.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ")
    private String refreshToken;
}
