package gift.external.kakao.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RefreshResponseDto(String tokenType,
                                 String accessToken,
                                 Integer expiresIn,
                                 String refreshToken,
                                 String refreshTokenExpiresIn) {
}
