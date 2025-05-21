package commitcapstone.commit.exer.service;

import commitcapstone.commit.auth.entity.User;
import commitcapstone.commit.auth.repository.UserRepository;
import commitcapstone.commit.exer.dto.request.TimeStatisticsRequest;
import commitcapstone.commit.exer.dto.response.PeriodDurationResponse;
import commitcapstone.commit.exer.entity.Work;
import commitcapstone.commit.exer.repository.WorkRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
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

        long durations = (long)workRepository.findDurationByUserId(user.getId());
        return durations;
    }

    public List<PeriodDurationResponse> getUserExerTime(String email, TimeStatisticsRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));
        LocalDate now = LocalDate.now();

        Long userId = user.getId();

        String period = request.getPeriod();
        LocalDate firstDate = request.getFirstDate();
        LocalDate endDate = firstDate;

        switch (period) {
            case "day":
                endDate = firstDate;
                break;
            case "week":
                endDate = firstDate.plusDays(6);
                break;
            case "month":
                endDate = firstDate.plusDays(29);
                break;
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }

        return workRepository.findDailyDurationByUserAndDateRange(userId, firstDate, endDate);




    }


}
