package com.hhp.ecommerce.presentation.api;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(ProductController.class)
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setup(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))  // RestDocs 설정 적용
			.alwaysDo(document("{method-name}"))  // 각 테스트 메서드마다 기본 문서화 설정 추가
			.build();
	}

	@Test
	void testGetProducts() throws Exception {
		mockMvc.perform(get("/products")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())  // 성공 상태 코드 확인
			.andDo(document("get-products",  // RestDocs 문서화 설정
				responseFields(
					fieldWithPath("[].productId").description("상품 ID"),
					fieldWithPath("[].name").description("상품명"),
					fieldWithPath("[].price").description("상품 가격"),
					fieldWithPath("[].stock").description("남은 재고")
				)
			));
	}

	@Test
	void testGetTopProducts() throws Exception {
		mockMvc.perform(get("/products/top")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("get-top-products",
				responseFields(
					fieldWithPath("[].productId").description("상품 ID"),
					fieldWithPath("[].name").description("상품명"),
					fieldWithPath("[].price").description("상품 가격"),
					fieldWithPath("[].totalSales").description("최근 3일간의 총 판매 수량")
				)
			));
	}
}
