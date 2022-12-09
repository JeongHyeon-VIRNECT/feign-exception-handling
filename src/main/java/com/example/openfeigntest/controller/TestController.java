package com.example.openfeigntest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.openfeigntest.dto.ProductRequestDto;
import com.example.openfeigntest.feign.TestClient;

import feign.Response;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
	private final TestClient testClient;

	@PostMapping("/create-product")
	public ResponseEntity<ProductRequestDto> createProduct(@RequestBody ProductRequestDto productRequestDto) {
		try {
			Response externalServiceResponse = testClient.createProduct(productRequestDto);
			System.out.println("externalServiceResponse = " + externalServiceResponse);
		} catch (Exception e) {
			System.out.println("e = " + e);
		}
		return new ResponseEntity<>(productRequestDto, HttpStatus.OK);
	}
}
