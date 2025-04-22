package commitcapstone.commit.auth.dto;

import commitcapstone.commit.auth.entity.Gender;
import commitcapstone.commit.auth.entity.Tier;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class SignInRequestDto {

    private String loginId;

    private String email;

    private String password;

}

