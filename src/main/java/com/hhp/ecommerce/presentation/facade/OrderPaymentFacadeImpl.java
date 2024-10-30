package com.hhp.ecommerce.presentation.facade;

import com.hhp.ecommerce.application.service.OrderService;
import com.hhp.ecommerce.application.service.PaymentService;
import com.hhp.ecommerce.application.service.ProductService;
import com.hhp.ecommerce.application.service.UserService;
import com.hhp.ecommerce.domain.model.Order;
import com.hhp.ecommerce.domain.model.OrderItem;
import com.hhp.ecommerce.domain.model.OrderStatus;
import com.hhp.ecommerce.domain.model.PaymentStatus;
import com.hhp.ecommerce.presentation.dto.OrderRequest;
import com.hhp.ecommerce.presentation.dto.PaymentResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderPaymentFacadeImpl implements OrderPaymentFacade {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final ProductService productService;
    private final UserService userService;

    @Transactional
    @Override
    public PaymentResponse createOrderAndProcessPayment(OrderRequest orderRequest, String idempotencyKey) {

        // Step 1: 주문 항목을 확인하고 OrderItem으로 변환
        var orderItem = orderRequest.getItems().stream()
                .map(item -> {
                    productService.checkStockAvailability(item.getProductId(), item.getQuantity());
                    int price = productService.getProductPrice(item.getProductId());
                    return OrderItem.create(item.getProductId(), item.getQuantity(), price);
                }).collect(Collectors.toList());

        // Step 2: 주문 생성
        Order order = Order.create(orderRequest.getUserId(), orderItem);
        order = orderService.createOrder(order);

        // Step 3: 사용자 잔액 확인
        int totalPrice = order.getTotalPrice();
        var user = userService.findUserById(orderRequest.getUserId());
        Long orderId = order.getId();
        if (user.getBalance() < totalPrice) {
            orderService.updateOrderStatus(orderId, OrderStatus.FAILED.name());
        }

        // Step 4: 결제 처리
        Payment payment = paymentService.processPayment(orderId, idempotencyKey, totalPrice, user.getId());

        // 결제 상태 확인 및 처리
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            orderService.updateOrderStatus(orderId, OrderStatus.SUCCESS.name());
            return new PaymentResponse(true, "결제 및 주문 완료");
        } else {
            orderService.updateOrderStatus(orderId, OrderStatus.FAILED.name());
            return new PaymentResponse(false, "결제 실패");
        }
    }
}
