package com.example.openfeigntest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.openfeigntest.dto.ProductRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class RestApiTest {
	private static final String URL = "/api/test/create-product";
	@Autowired
	public MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
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
	void externalInternalServerErrorTest() throws Exception {
		ProductRequestDto productRequestDto = new ProductRequestDto();
		productRequestDto.setId(100L);
		productRequestDto.setName("test");
		productRequestDto.setPrice(1000D);
		productRequestDto.setCreatedAt(LocalDateTime.now());

		stubFor(post(urlEqualTo("/products"))
			.willReturn(aResponse().withStatus(500)));

		mockMvc.perform(
				MockMvcRequestBuilders.post(URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(productRequestDto))
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void externalBadRequestErrorTest() throws Exception {
		ProductRequestDto productRequestDto = new ProductRequestDto();
		productRequestDto.setId(100L);
		productRequestDto.setName("test");
		productRequestDto.setPrice(1000D);
		productRequestDto.setCreatedAt(LocalDateTime.now());

		stubFor(post(urlEqualTo("/products"))
			.willReturn(aResponse().withStatus(400)));

		mockMvc.perform(
				MockMvcRequestBuilders.post(URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(productRequestDto))
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void externalNotFoundErrorTest() throws Exception {
		ProductRequestDto productRequestDto = new ProductRequestDto();
		productRequestDto.setId(100L);
		productRequestDto.setName("test");
		productRequestDto.setPrice(1000D);
		productRequestDto.setCreatedAt(LocalDateTime.now());

		stubFor(post(urlEqualTo("/products"))
			.willReturn(aResponse().withStatus(404)));

		mockMvc.perform(
				MockMvcRequestBuilders.post(URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(productRequestDto))
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void externalServiceReadTimeoutErrorTest() throws Exception {
		ProductRequestDto productRequestDto = new ProductRequestDto();
		productRequestDto.setId(100L);
		productRequestDto.setName("test");
		productRequestDto.setPrice(1000D);
		productRequestDto.setCreatedAt(LocalDateTime.now());

		stubFor(post(urlEqualTo("/products"))
			.willReturn(aResponse().withFixedDelay((int)Duration.ofSeconds(15).toMillis())));

		mockMvc.perform(
				MockMvcRequestBuilders.post(URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(productRequestDto))
			).andDo(print())
			.andExpect(status().isOk());
	}
}
