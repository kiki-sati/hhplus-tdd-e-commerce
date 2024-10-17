package com.hhp.ecommerce.presentation.api;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
import com.hhp.ecommerce.presentation.dto.BalanceRequest;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(BalanceController.class)
public class BalanceControllerTest {

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
	public void testChargeBalance() throws Exception {
		BalanceRequest requestDto = new BalanceRequest("user1", 1000);

		mockMvc.perform(post("/balance/charge")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDto)))
			.andExpect(status().isOk())
			.andDo(document("charge-balance",  // RestDocs 문서화 설정
				requestFields(
					fieldWithPath("userId").description("충전할 사용자 ID"),
					fieldWithPath("amount").description("충전할 금액")
				),
				responseFields(
					fieldWithPath("userId").description("사용자 ID"),
					fieldWithPath("currentBalance").description("충전 후 현재 잔액")
				)
			));
	}

	@Test
	public void testGetBalance() throws Exception {
		mockMvc.perform(get("/balance")
				.param("userId", "user1")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())  // 성공 상태 코드 확인
			.andDo(document("get-balance",  // RestDocs 문서화 설정
				queryParameters(
					parameterWithName("userId").description("잔액을 조회할 사용자 ID")
				),
				responseFields(
					fieldWithPath("userId").description("사용자 ID"),
					fieldWithPath("currentBalance").description("현재 잔액")
				)
			));
	}
}
