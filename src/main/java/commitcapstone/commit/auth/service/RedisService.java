package commitcapstone.commit.auth.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(String email, String refreshToken, long expirationSeconds) {
        redisTemplate.opsForValue().set(email, refreshToken, expirationSeconds, TimeUnit.SECONDS);
    }

    public String getRefreshToken(String email) {
        var value = redisTemplate.opsForValue().get(email);
        return value != null ? value.toString() : null;
    }

    public void deleteRefreshToken(String email) {
        redisTemplate.delete(email);
    }

    public boolean hasRefreshToken(String email) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(email));
    }

}
