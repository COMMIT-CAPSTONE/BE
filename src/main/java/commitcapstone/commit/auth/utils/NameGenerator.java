package commitcapstone.commit.auth.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class NameGenerator {
    private static final String[] ACTIONS = {
            "벤치프레스", "스쿼트", "데드리프트", "러닝", "사이클", "풀업", "푸쉬업", "플랭크", "런지", "버피"
    };

    private static final Random RANDOM = new Random();

    public static String generateDoitName() {
        String action = ACTIONS[RANDOM.nextInt(ACTIONS.length)];
        return action + " 하는 두잇이";
    }
}
