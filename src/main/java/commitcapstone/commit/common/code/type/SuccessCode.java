package commitcapstone.commit.common.code.type;

import org.springframework.http.HttpStatus;

public interface SuccessCode {
    HttpStatus getHttpstatus();
    String getMessage();
}
