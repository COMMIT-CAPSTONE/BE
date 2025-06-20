package commitcapstone.commit.challenge.dto;

import commitcapstone.commit.challenge.entity.ChallengeType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ChallengeCreateRequest {

    @NotNull(message = "챌린지 제목은 필수입니다.")
    private String challengeTitle;

    @NotNull(message = "챌린지 설명은 필수입니다.")
    private String challengeDescription;

    @NotNull(message = "챌린지 유형은 필수입니다.")
    private ChallengeType challengeType;

    @NotNull(message = "베팅 포인트는 필수입니다.")
    private Integer betPoint;

    @NotNull(message = "목표 시간(분)은 필수입니다.")
    private Integer targetMinutes;

    @NotNull(message = "시작일은 필수입니다.")
    private LocalDate startDate;

    @NotNull(message = "종료일은 필수입니다.")
    private LocalDate endDate;
}
