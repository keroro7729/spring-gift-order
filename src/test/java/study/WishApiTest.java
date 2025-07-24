package study;

import gift.Application;
import gift.common.dto.request.AddWishRequestDto;
import gift.common.dto.request.MemberRequestDto;
import gift.common.dto.response.TokenResponseDto;
import gift.common.dto.response.WishResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        classes = Application.class
)
public class WishApiTest {

    private static final RestClient client = RestClient.builder().build();

    @LocalServerPort
    private int port;

    private static final String email = "wish-test-email@gmail.com";
    private static final String password = "test-password123!@#";
    private static String token;
    private static Long createdWishId;

    @BeforeAll
    void initToken() {
        String url = "http://localhost:" + port + "/api/members/register";
        MemberRequestDto request1 = new MemberRequestDto(email, password);

        ResponseEntity<TokenResponseDto> response1 =
                client.post()
                        .uri(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(request1)
                        .retrieve()
                        .toEntity(TokenResponseDto.class);

        token = response1.getBody().token();

        url = "http://localhost:" + port + "/api/wishes/add";
        AddWishRequestDto request2 = new AddWishRequestDto(1L, 10);

        ResponseEntity<WishResponseDto> response2 =
                client.post()
                        .uri(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(request2)
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .toEntity(WishResponseDto.class);

        createdWishId = response2.getBody().id();
    }

    @Test
    void 관심상품_등록시_200과_Wish응답_반환() {
        String url = "http://localhost:" + port + "/api/wishes/add";
        AddWishRequestDto request = new AddWishRequestDto(1L, 10);

        ResponseEntity<WishResponseDto> response =
                client.post()
                        .uri(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(request)
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .toEntity(WishResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().productId()).isEqualTo(1L);
        System.out.println("관심상품_등록시_200과_Wish응답_반환: " + response.getBody().toString());
    }

    @Test
    void 관심상품_조회시_200과_위시리스트_리턴() {
        String url = "http://localhost:" + port + "/api/wishes";

        ResponseEntity<List<WishResponseDto>> response =
                client.get()
                        .uri(url)
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .toEntity(new ParameterizedTypeReference<List<WishResponseDto>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        response.getBody().stream()
                .forEach(wish -> System.out.println("관심상품_조회시_200과_위시리스트_리턴: " + wish));
    }

    @Test
    void 관심상품_삭제시_204반환() {
        String url = "http://localhost:" + port + "/api/wishes/" + createdWishId;

        ResponseEntity<Void> response =
                client.delete()
                        .uri(url)
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
