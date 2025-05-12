package commitcapstone.commit.oauth.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
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
            Claims claim = (Claims) Jwts.claims()
                    .setIssuer("Commit")
                    .setSubject("Token")
                    .setAudience(email)
                    .setIssuedAt(now)
                    .setExpiration(tokenDate);


            return Jwts.builder()
                    .setClaims(claim)
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
            Claims claim = (Claims) Jwts.claims()
                    .setIssuer("Commit")
                    .setSubject("Token")
                    .setAudience(email)
                    .setIssuedAt(now)
                    .setExpiration(tokenDate);


            return Jwts.builder()
                    .setClaims(claim)
                    .signWith(key)
                    .compact();

        } catch (Exception e) {
            throw new RuntimeException("failed create Access Token", e);
        }
    }

    public Claims validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new RuntimeException("Token is null or empty");
        }
        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .requireIssuer("Commit")
                    .requireSubject("Token")
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getAudience().toString();
            if (email == null || email.trim().isEmpty()) {
                throw new RuntimeException("Invalid Token");
            }



            return claims;
        } catch (Exception e) {
            throw new RuntimeException("Invalid Token ", e);
        }

    }
}