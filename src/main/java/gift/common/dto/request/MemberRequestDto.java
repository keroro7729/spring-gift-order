package gift.common.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record MemberRequestDto(
        @Email(message = "이메일 포멧을 입력해주세요.") String email,
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{6,30}$",
                message = "길이 6 ~ 30 사이의 영어, 숫자, 특수문자를 포함한 비밀번호를 입력해주세요.\n특수문자: !@#$%^*+=-")
        String password) {
}
