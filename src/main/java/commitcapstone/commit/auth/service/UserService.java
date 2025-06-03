package commitcapstone.commit.auth.service;

import commitcapstone.commit.auth.config.jwt.JwtTokenProvider;
import commitcapstone.commit.auth.dto.request.UserInfoRequest;
import commitcapstone.commit.auth.entity.Gym;
import commitcapstone.commit.auth.entity.User;
import commitcapstone.commit.auth.repository.GymRepository;
import commitcapstone.commit.auth.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final GymRepository gymRepository;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, GymRepository gymRepository, RedisService redisService, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.gymRepository = gymRepository;
        this.redisService = redisService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    public void setUserInfo(UserInfoRequest userInfo, String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        String email = jwtTokenProvider.getUserEmail(token);
        String provider = redisService.get("provider:" + email);
        String oauthId = redisService.get("oauthId:" + email);
        User user = new User();
        user.setOauthProvider(provider);
        user.setOauthId(oauthId);
        user.setEmail(email);

        user.setName(userInfo.getNickName());

        Gym gym = new Gym();
        gym.setName(userInfo.getGymName());
        gym.setAddress(userInfo.getGymAddress());
        gym.setLatitude(userInfo.getLatitude());
        gym.setLongitude(userInfo.getLongitude());


        //OneToOne
        user.setGym(gym);
        gym.setUser(user);

        userRepository.save(user);
    }


}
