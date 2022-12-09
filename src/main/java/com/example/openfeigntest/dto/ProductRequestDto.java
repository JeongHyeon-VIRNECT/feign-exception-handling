package com.example.openfeigntest.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {
	private Long id;
	private String name;
	private Double price;
	private LocalDateTime createdAt;

	@Override
	public String toString() {
		return "Product{" +
			"id=" + id +
			", name='" + name + '\'' +
			", price=" + price +
			", createdAt=" + createdAt +
			'}';
	}
}
