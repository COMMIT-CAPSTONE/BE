package commitcapstone.commit.auth.provider.google.dto;

import lombok.Data;

@Data
public class googleValidToken {

    private String sub;
    private String name;
    private String given_name;
    private String picture;
    private String email;


}
