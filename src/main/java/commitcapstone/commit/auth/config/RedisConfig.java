package commitcapstone.commit.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    // 환경변수 또는 application.yml이 있을 경우 해당 값을 자동 주입
    @Value("${REDISHOST:localhost}")
    private String redisHost;

    @Value("${REDISPORT:6379}")
    private int redisPort;

    @Value("${REDISPASSWORD:}")
    private String redisPassword;

    // 수동으로 Redis 연결 팩토리 생성
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        if (redisPassword != null && !redisPassword.isBlank()) {
            config.setPassword(RedisPassword.of(redisPassword));
        }
        return new LettuceConnectionFactory(config);
    }

    // RedisTemplate 설정
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer()); // 필요에 따라 변경 가능
        template.afterPropertiesSet();
        return template;
    }
}
