package study;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.Application;
import gift.common.dto.request.MemberRequestDto;
import gift.common.dto.response.MemberResponseDto;
import gift.common.dto.response.TokenResponseDto;
import org.antlr.v4.runtime.Token;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        classes = Application.class
)
@ExtendWith(SoftAssertionsExtension.class)
public class MemberApiTest {

    private final RestClient client = RestClient.builder().build();

    @LocalServerPort
    private int port;

    private static final String email = "member-test-email@gmail.com";
    private static final String password = "test-password123!@#";
    private String token;

    @BeforeAll
    void initToken() {
        String url = "http://localhost:" + port + "/api/members/register";
        MemberRequestDto request = new MemberRequestDto(email, password);

        ResponseEntity<TokenResponseDto> response =
                client.post()
                        .uri(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(request)
                        .retrieve()
                        .toEntity(TokenResponseDto.class);

        token = response.getBody().token();
    }

    @Test
    void 회원가입_성공시_201과_Token반환(SoftAssertions softly) {
        String url = "http://localhost:" + port + "/api/members/register";
        MemberRequestDto request = new MemberRequestDto("test-email@naver.com", "testpassword123!@#");

        ResponseEntity<TokenResponseDto> response =
                client.post()
                        .uri(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(request)
                        .retrieve()
                        .toEntity(TokenResponseDto.class);

        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        softly.assertThat(response.getBody().token()).isNotNull();
        System.out.println("회원가입_성공시_201과_Token반환: token=" + response.getBody().token());
    }

    @Test
    void 로그인_성공시_200과_Token반환(SoftAssertions softly) {
        String url = "http://localhost:" + port + "/api/members/login";
        MemberRequestDto request = new MemberRequestDto(email, password);

        ResponseEntity<TokenResponseDto> response =
                client.post()
                        .uri(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(request)
                        .retrieve()
                        .toEntity(TokenResponseDto.class);

        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        softly.assertThat(response.getBody().token()).isNotNull();
        System.out.println("로그인_성공시_200과_Token반환: token=" + response.getBody().token());
    }
}
