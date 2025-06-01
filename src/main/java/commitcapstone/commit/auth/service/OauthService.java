package commitcapstone.commit.auth.service;

import commitcapstone.commit.auth.config.jwt.JwtTokenProvider;
import commitcapstone.commit.auth.dto.oauth.OauthProvider;
import commitcapstone.commit.auth.dto.oauth.OauthUserInfo;
import commitcapstone.commit.auth.dto.oauth.TokenRequest;
import commitcapstone.commit.auth.dto.request.RefreshTokenRequest;
import commitcapstone.commit.auth.dto.response.LoginResponse;
import commitcapstone.commit.auth.repository.UserRepository;
import commitcapstone.commit.common.code.OauthErrorCode;
import commitcapstone.commit.common.exception.OauthException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OauthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    public OauthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, RedisService redisService) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisService = redisService;
    }

    /**
     * 로그인 처리 Service 코드
     * @param tokenRequest 클라이언트로부터 받은 accessToken과 provider 정보를 포함
     * @return : LoginRespones : tokenType, accessToken, refreshToken, status
     * status - 사용자 계정이 이미 존재하는지 확인
     * 존재 - not_fitst_login
     * 미존재 - first_login
     *
     * 1. 클라이언트에게 받은 정보가 정확한지 검증
     * 2. accessToken, refreshToken 생성
     * 3. redis에 refreshToken, oauthId, provider 저장 oauthId와 provider은 사용자 정보 입력후 한번에 db저장을 위해 잠시 저장
     * 4. 클라이언트에게 LoginResponse 전달
     * */
    public LoginResponse login(TokenRequest tokenRequest) {
        String provider = tokenRequest.getProvider(); //소셜로그인 제공자 : GOOGLE,KAKAO,NAVER
        String oauthAccessToken = tokenRequest.getAccessToken();

        if (provider == null || provider.trim().isEmpty()) {
            throw new OauthException(OauthErrorCode.UNSUPPORTED_NULL_PROVIDER);
        }

        switch (provider.toUpperCase()) {
            case "GOOGLE":
            case "KAKAO":
            case "NAVER":
                break;
            default:
                throw new OauthException(OauthErrorCode.UNSUPPORTED_PROVIDER);
        }

        if (oauthAccessToken.isEmpty()) {
            throw new OauthException(OauthErrorCode.IS_EMPTY_OAUTH_ACCESS_TOKEN);
        }

        OauthUserInfo userInfo = getUserInfo(
                OauthProvider.valueOf(provider.toUpperCase()), oauthAccessToken
        );

        String userId = userInfo.getId();
        String userEmail = userInfo.getEmail();


        String accessToken = jwtTokenProvider.createAccessToken(userEmail);
        String refreshToken = jwtTokenProvider.createRefreshToken(userEmail);

        String status;
        if (isUserCheck(provider , userEmail)) {
            status = "not_first_login";
        } else {
            status = "first_login";
        }

        redisService.save("refreshToken:" + userEmail, refreshToken, 604800000); // 7일 저장
        redisService.save("provider:" + userEmail, provider, 1800000 ); // 30분 저장
        redisService.save("oauthId:" + userEmail, userId, 1800000 ); // 30분 저장

        return new LoginResponse("Bearer", accessToken, refreshToken, status);
    }

    /**
     * 로그아웃 처리 Service 코드
     * @param request : refreshToken
     * 1. 클라이언트에게 받은 정보 검증(refreshToken)
     * 2. redis에서 refreshToken 꺼낸 후 받은 refreshToken과 비교검증
     * 3. redis에서 refreshToken 삭제
     * */
    public void logout(RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new OauthException(OauthErrorCode.INVALID_REFRESH_TOKEN);
        }

        String email = jwtTokenProvider.getUserEmail(refreshToken);
        String refreshTokenRedisKey = "refreshToken:" + email;
        String storedRefreshToken = redisService.get(refreshTokenRedisKey);

        if (storedRefreshToken == null) {
            throw new OauthException(OauthErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }
        if (!storedRefreshToken.equals(refreshToken)) {
            throw new OauthException(OauthErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        redisService.deleteRefreshToken(refreshTokenRedisKey);
    }

    /*
    *
    *
    * */
    public String refresh(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new OauthException(OauthErrorCode.INVALID_REFRESH_TOKEN);
        }

        String email = jwtTokenProvider.getUserEmail(refreshToken);
        String storedRefreshToken = redisService.get(email);

        if (storedRefreshToken == null) {
            throw new OauthException(OauthErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }
        if (!storedRefreshToken.equals(refreshToken)) {
            throw new OauthException(OauthErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        return jwtTokenProvider.createAccessToken(email);
    }










    public OauthUserInfo getUserInfo(OauthProvider oauthProvider, String accessToken) {
        WebClient client = WebClient.builder()
                .baseUrl(oauthProvider.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, oauthProvider.getHeaderType())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .build();

        return client.post()
                .retrieve()
                .bodyToMono(oauthProvider.getResponseType())
                .block();
    }

    public boolean isUserCheck(String provider, String email) {
        if (userRepository.existsByOauthProviderAndEmail(provider,email)) {
            return true;
        } else {
            return false;
        }
    }
}