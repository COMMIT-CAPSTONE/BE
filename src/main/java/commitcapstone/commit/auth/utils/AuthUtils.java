package commitcapstone.commit.auth.utils;

import commitcapstone.commit.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final UserRepository userRepository;
    private final NameGenerator nameGenerator;

    @Bean
    public String generateUniqueDoitName() {
        String baseName = nameGenerator.generateDoitName();
        String name = baseName;
        int suffix = 1;
        while (userRepository.existsByName(name)) {
            name = baseName + suffix;
            suffix++;
        }
        return name;
    }

}
