package commitcapstone.commit.exer.service;

import commitcapstone.commit.auth.entity.User;
import commitcapstone.commit.auth.repository.UserRepository;
import commitcapstone.commit.exer.entity.Work;
import commitcapstone.commit.exer.repository.WorkRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExerService {
    private final UserRepository userRepository;
    private final WorkRepository workRepository;
    public ExerService(UserRepository userRepository, WorkRepository workRepository) {
        this.userRepository = userRepository;
        this.workRepository = workRepository;
    }

    public void saveTime(String email, long min) {
        LocalDate now = LocalDate.now();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));

        Work work = new Work();
        work.setDuration(min);
        work.setWorkDate(now);
        work.setUser(user);

        workRepository.save(work);
    }

    public long getExerTime(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));

        List<Long> durations = workRepository.findDurationByUserId(user.getId());
        return durations.stream()
                .mapToLong(value -> value.longValue()) // Long -> long
                .sum();
    }
}
