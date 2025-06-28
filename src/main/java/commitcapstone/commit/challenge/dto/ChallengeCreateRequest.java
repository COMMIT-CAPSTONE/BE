package commitcapstone.commit.challenge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import commitcapstone.commit.challenge.entity.ChallengeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ChallengeCreateRequest {

    @Schema(description = "챌린지 제목", example = "아침 6시 기상 챌린지")
    private String challengeTitle;

    @Schema(description = "챌린지 설명", example = "매일 아침 6시에 일어나기")
    private String challengeDescription;

    @Schema(description = "챌린지 타입", example = "TIME")
    private ChallengeType challengeType;

    @Schema(description = "베팅 포인트", example = "1000")
    private int betPoint;

    @Schema(description = "목표 시간(분)", example = "60")
    private int targetMinutes;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "시작 날짜", example = "2024-06-01")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "종료 날짜", example = "2024-06-30")
    private LocalDate endDate;
}