package commitcapstone.commit.oauth.google.dto;

import lombok.Data;

@Data
public class googleInfo {

    private String sub;
    private String name;
    private String given_name;
    private String picture;
    private String email;
    private boolean email_verified;

}
