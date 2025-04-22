package commitcapstone.commit.auth.config.config;

import commitcapstone.commit.auth.config.jwt.JwtAuthFilter;
import commitcapstone.commit.auth.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationCodeGrantFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
//    private final JwtAuthFilter jwtAuthFilter;
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
//        this.jwtAuthFilter = jwtAuthFilter;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.
                csrf(c -> c.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/*", "/oauth/**", "login/*").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(
                        oauth ->
                            oauth.userInfoEndpoint(
                                    endpoint -> endpoint.userService(customOAuth2UserService)
                            )
                );
//                .addFilterBefore(jwtAuthFilter,
//                        UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
