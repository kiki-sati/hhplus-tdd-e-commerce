package com.hhp.ecommerce.presentation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhp.ecommerce.presentation.dto.OrderRequest;
import com.hhp.ecommerce.presentation.dto.PaymentResponse;
import com.hhp.ecommerce.presentation.facade.OrderPaymentFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderPaymentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderPaymentFacade orderPaymentFacade;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderRequest orderRequest;
    private String idempotencyKey = "unique_key_1234";

    @BeforeEach
    public void setup() {
        orderRequest = new OrderRequest(
                1L,
                List.of(
                        new OrderRequest.OrderItem(101L, 2, 10000),
                        new OrderRequest.OrderItem(102L, 1, 15000)
                )
        );
    }

    @Test
    public void testCreateOrderAndProcessPayment_Success() throws Exception {
        // 결제 성공 응답 모의
        PaymentResponse paymentResponse = new PaymentResponse(true, "결제 및 주문 완료");
        Mockito.when(orderPaymentFacade.createOrderAndProcessPayment(any(OrderRequest.class), eq(idempotencyKey)))
                .thenReturn(paymentResponse);

        mockMvc.perform(post("/order-payment/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Idempotency-Key", idempotencyKey)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(paymentResponse)));
    }

    @Test
    public void testCreateOrderAndProcessPayment_FailureDueToInsufficientBalance() throws Exception {
        // 잔액 부족으로 인한 결제 실패 응답 모의
        PaymentResponse paymentResponse = new PaymentResponse(false, "잔액 부족으로 결제 실패");
        Mockito.when(orderPaymentFacade.createOrderAndProcessPayment(any(OrderRequest.class), eq(idempotencyKey)))
                .thenReturn(paymentResponse);

        mockMvc.perform(post("/order-payment/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Idempotency-Key", idempotencyKey)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(paymentResponse)));
    }
}
