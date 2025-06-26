package commitcapstone.commit.common;

import commitcapstone.commit.common.exception.ChallengeException;
import commitcapstone.commit.common.exception.ExerException;
import commitcapstone.commit.common.exception.OauthException;
import commitcapstone.commit.common.exception.UserException;
import commitcapstone.commit.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ex.printStackTrace();  // 로그 남기기
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("서버 에러 발생" + ex.getMessage()));
    }
    //통합 에러 처리
    @ExceptionHandler(OauthException.class)
    public ResponseEntity<ErrorResponse> handleOauthException(OauthException e) {
        log.error("OauthException", e);
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus().value())
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ExerException.class)
    public ResponseEntity<ErrorResponse> handleOauthException(ExerException e) {
        log.error("ExerException", e);
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus().value())
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ChallengeException.class)
    public ResponseEntity<ErrorResponse> handleOauthException(ChallengeException e) {
        log.error("ExerException", e);
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus().value())
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleOauthException(UserException e) {
        log.error("ExerException", e);
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus().value())
                .body(new ErrorResponse(e.getMessage()));
    }

}
