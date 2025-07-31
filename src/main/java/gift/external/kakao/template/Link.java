package gift.external.kakao.template;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Link(String webUrl, String mobileWebUrl, String androidExecutionParams, String iosExecutionParams) {
    public static Link of(String webUrl) {
        return new Link(webUrl, null, null, null);
    }
}
