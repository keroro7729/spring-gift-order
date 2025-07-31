package gift.external.kakao.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TemplateObject(String objectType, String text, Link link) {
    public static TemplateObject of(String text, String url) {
        return new TemplateObject("text", text, Link.of(url));
    }

    public MultiValueMap<String, String> toFormData() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(this);
            formData.add("template_object", json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize template_object", e);
        }
        return formData;
    }
}
