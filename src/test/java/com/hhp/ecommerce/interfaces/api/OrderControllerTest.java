package com.hhp.ecommerce.interfaces.api;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

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
import com.hhp.ecommerce.interfaces.dto.OrderItemDto;
import com.hhp.ecommerce.interfaces.dto.OrderRequest;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(OrderController.class)
class OrderControllerTest {

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
	void testCreateOrder() throws Exception {
		// Mock 주문 요청 생성
		OrderRequest requestDto = new OrderRequest("user1", Arrays.asList(
			new OrderItemDto("abc123", 2),
			new OrderItemDto("xyz789", 1)
		));

		mockMvc.perform(post("/order")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDto)))
			.andExpect(status().isOk())
			.andDo(document("create-order",
				requestFields(
					fieldWithPath("userId").description("주문할 사용자 ID"),
					fieldWithPath("items[].productId").description("주문할 상품 ID"),
					fieldWithPath("items[].quantity").description("주문할 상품 수량")
				),
				responseFields(
					fieldWithPath("orderId").description("생성된 주문 ID"),
					fieldWithPath("userId").description("주문한 사용자 ID"),
					fieldWithPath("totalPrice").description("총 결제 금액"),
					fieldWithPath("orderStatus").description("주문 상태 (SUCCESS, FAILED)")
				)
			));
	}
}
