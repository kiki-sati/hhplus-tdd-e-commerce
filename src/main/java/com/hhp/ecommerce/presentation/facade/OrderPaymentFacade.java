package com.hhp.ecommerce.presentation.facade;

import com.hhp.ecommerce.presentation.dto.OrderRequest;
import com.hhp.ecommerce.presentation.dto.OrderResponse;
import com.hhp.ecommerce.presentation.dto.PaymentRequest;
import com.hhp.ecommerce.presentation.dto.PaymentResponse;

public interface OrderPaymentFacade {
    PaymentResponse createOrderAndProcessPayment(OrderRequest orderRequest, String idempotencyKey);
}