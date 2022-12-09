package com.example.openfeigntest.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.openfeigntest.dto.ProductRequestDto;

import feign.Response;

@FeignClient(name = "test-client", url = "http://localhost:8081/products")
public interface TestClient {
	@PostMapping
	Response createProduct(ProductRequestDto productRequestDto);
}
