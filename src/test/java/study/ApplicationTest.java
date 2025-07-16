package study;

import gift.common.dto.request.ProductRequestDto;
import gift.common.dto.response.MessageResponseDto;
import gift.common.dto.response.ProductResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ApplicationTest {

    private final RestClient client = RestClient.builder().build();
    private final List<ProductResponseDto> predefined = new ArrayList<>();
    @LocalServerPort
    private int port;

    public ApplicationTest() {
        predefined.add(new ProductResponseDto(1L, "아메리카노", 3000L, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR3bgpr9EPuJ47gcYCWg7jrEXJ3M15nEXZ9WdKpUsF11wMJFwIPXpOtIkDwoTUUi8_S_WbVTmcus1R7oEx0ongOCiJtjK8iLm-JxAp4swI_-Q", "판매중"));
        predefined.add(new ProductResponseDto(2L, "카페라떼", 4000L, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjYwdtYk0ww-YSRxAG1stQYFuTT6K2D5lQcQ&s", "판매중"));
        predefined.add(new ProductResponseDto(3L, "모카", 5000L, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkd11qAyK1kPY8z6tpvKO4KM97cTpCphVeOQ&s", "판매중"));
        predefined.add(new ProductResponseDto(4L, "아포가토", 4500L, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSvgoCg5CPPBL8MZGAWT3ilkSeBnr1SkR-x2A&s", "판매중"));
    }

    @Test
    void 상품_생성시_201과_생성된_상품정보_반환() {
        var url = "http://localhost:" + port + "/api/products";
        var response = client.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ProductRequestDto("coffee", 3500L, "test-url"))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<MessageResponseDto<ProductResponseDto>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        var locationId = response.getHeaders().getLocation().toString().split("/")[3];
        var expectedId = Long.parseLong(locationId);
        var data = response.getBody().data();
        assertBody(data, expectedId, "coffee", 3500L, "test-url");

        // reset
        client.delete().uri(url + "/" + expectedId);
    }

    @Test
    void 상품_조회시_200과_해당_상품정보_반환() {
        var url = "http://localhost:" + port + "/api/products/1";
        var response = client.get()
                .uri(url)
                .retrieve()
                .toEntity(ProductResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var expected = predefined.get(0);
        assertBody(response.getBody(), expected.id(), expected.name(), expected.price(), expected.imageUrl());
    }

    @Test
    void 상품_수정시_200과_수정된_상품정보_반환() {
        var url = "http://localhost:" + port + "/api/products/2";
        var response = client.put()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ProductRequestDto("coffee", 3500L, "test-url"))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<MessageResponseDto<ProductResponseDto>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var data = response.getBody().data();
        assertBody(data, 2L, "coffee", 3500L, "test-url");

        // reset
        var reset = predefined.get(1);
        client.put()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ProductRequestDto(reset.name(), reset.price(), reset.imageUrl()));
    }

    @Test
    void 상품_삭제시_204_반환() {
        var url = "http://localhost:" + port + "/api/products/3";
        var response = client.delete()
                .uri(url)
                .retrieve()
                .toBodilessEntity();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void 존재하지_않는_ID로_접근시_404반환() throws IOException {
        var url = "http://localhost:" + port + "/api/products/100";
        var response_get = client.get()
                .uri(url)
                .exchange((req, res) -> res);
        var response_put = client.put()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ProductRequestDto("name", 0L, "test-url"))
                .exchange((req, res) -> res);
        var response_delete = client.delete()
                .uri(url)
                .exchange((req, res) -> res);

        assertThat(response_get.getStatusCode())
                .as("GET 요청시 404 반환 여부 확인")
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response_put.getStatusCode())
                .as("PUT 요청시 404 반환 여부 확인")
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response_delete.getStatusCode())
                .as("DELETE 요청시 404 반환 여부 확인")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void 잘못된_이름의_상품_생성시_400반환() throws IOException {
        var url = "http://localhost:" + port + "/api/products";
        String[] badNames = {"길이 15 초과 상품 1234", "상품!", "상품@", "상품#", "상품$", "상품%", "상품^", "상품*", "상품=",
                "상품~", "상품`", "상품{", "상품}", "상품\\", "상품|", "상품;", "상품:", "상품?"};

        for (String name : badNames) {
            var response = client.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ProductRequestDto(name, 0L, "test-url"))
                    .exchange((req, res) -> res);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    void 카카오가_포함된_이름의_상품_생성시_202반환() {
        var url = "http://localhost:" + port + "/api/products";
        var response = client.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ProductRequestDto("카카오_관련상품", 1000000L, "test-url"))
                .retrieve()
                .toEntity(MessageResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    private void assertBody(ProductResponseDto actual, Long id, String name, Long price, String imageUrl) {
        assertThat(actual.id()).isEqualTo(id);
        assertThat(actual.name()).isEqualTo(name);
        assertThat(actual.price()).isEqualTo(price);
        assertThat(actual.imageUrl()).isEqualTo(imageUrl);
    }
}
