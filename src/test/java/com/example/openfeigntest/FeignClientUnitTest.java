package com.example.openfeigntest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.openfeigntest.dto.ProductRequestDto;
import com.example.openfeigntest.feign.TestClient;
import com.github.tomakehurst.wiremock.WireMockServer;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FeignClientUnitTest {
	@Autowired
	private TestClient testClient;
	private static WireMockServer wireMockServer;

	@BeforeAll
	static void startMock() {
		wireMockServer = new WireMockServer(8081);
		configureFor("localhost", 8081);
		wireMockServer.start();
	}

	@AfterAll
	static void stopMockServer() {
		wireMockServer.stop();
	}

	@Test
	void internalServerError() {
		ProductRequestDto productRequestDto = new ProductRequestDto();
		productRequestDto.setId(100L);
		productRequestDto.setName("test");
		productRequestDto.setPrice(1000D);
		productRequestDto.setCreatedAt(LocalDateTime.now());

		stubFor(post(urlEqualTo("/products"))
			.willReturn(aResponse().withStatus(500)));

		assertDoesNotThrow(() -> testClient.createProduct(productRequestDto));
	}

	@Test
	void notFoundError() {
		ProductRequestDto productRequestDto = new ProductRequestDto();
		productRequestDto.setId(100L);
		productRequestDto.setName("test");
		productRequestDto.setPrice(1000D);
		productRequestDto.setCreatedAt(LocalDateTime.now());

		stubFor(post(urlEqualTo("/products"))
			.willReturn(aResponse().withStatus(404)));

		assertDoesNotThrow(() -> testClient.createProduct(productRequestDto));
	}

	@Test
	void badRequestError() {
		ProductRequestDto productRequestDto = new ProductRequestDto();
		productRequestDto.setId(100L);
		productRequestDto.setName("test");
		productRequestDto.setPrice(1000D);
		productRequestDto.setCreatedAt(LocalDateTime.now());

		stubFor(post(urlEqualTo("/products"))
			.willReturn(aResponse().withStatus(400)));

		assertDoesNotThrow(() -> testClient.createProduct(productRequestDto));
	}
}
