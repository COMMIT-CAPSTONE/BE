package commitcapstone.commit.auth.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Long accessTokenTime;
    private final Long refreshTokenTime;
    private final Key key;

    public JwtTokenProvider(
            @Value("${spring.jwt.secret-key}") String secretKey,
            @Value("${spring.jwt.expires-in.access}") Long accessTokenTime,
            @Value("${spring.jwt.expires-in.refresh}") Long refreshTokenTime
    ) {
        this.accessTokenTime = accessTokenTime;
        this.refreshTokenTime = refreshTokenTime;
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }


    public String createAccessToken(String email) {



        try {
            Date now = new Date();
            Date tokenDate = new Date(now.getTime() + accessTokenTime);

            return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(now)
                    .setExpiration(tokenDate)
                    .signWith(key)
                    .compact();

        } catch (Exception e) {
            throw new RuntimeException("failed create Access Token", e);
        }

    }

    public String createRefreshToken(String email) {
        try {
            Date now = new Date();
            Date tokenDate = new Date(now.getTime() + refreshTokenTime);



            return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(now)
                    .setExpiration(tokenDate)
                    .signWith(key)
                    .compact();

        } catch (Exception e) {
            throw new RuntimeException("failed create Access Token", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUserEmail(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}