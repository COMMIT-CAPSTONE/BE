package commitcapstone.commit.common;

import commitcapstone.commit.common.exception.OauthException;
import commitcapstone.commit.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //통합 에러 처리
    @ExceptionHandler(OauthException.class)
    public ResponseEntity<ErrorResponse> handleOauthException(OauthException e) {
        log.error("OauthException", e);
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus().value())
                .body(new ErrorResponse(e.getMessage()));
    }
}
