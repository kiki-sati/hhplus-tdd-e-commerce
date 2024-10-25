package com.hhp.ecommerce.presentation.api;

import com.hhp.ecommerce.presentation.dto.OrderRequest;
import com.hhp.ecommerce.presentation.dto.PaymentResponse;
import com.hhp.ecommerce.presentation.facade.OrderPaymentFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/order-payment")
public class OrderPaymentController {

    private final OrderPaymentFacade orderPaymentFacade;

    @Autowired
    public OrderPaymentController(OrderPaymentFacade orderPaymentFacade) {
        this.orderPaymentFacade = orderPaymentFacade;
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> createOrderAndProcessPayment(
            @Valid @RequestBody OrderRequest orderRequest,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {

        PaymentResponse paymentResponse = orderPaymentFacade.createOrderAndProcessPayment(orderRequest, idempotencyKey);

        return paymentResponse.isPaymentSuccessful()
                ? ResponseEntity.ok(paymentResponse)
                : ResponseEntity.badRequest().body(paymentResponse);
    }
}
