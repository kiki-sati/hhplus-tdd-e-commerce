package com.hhp.ecommerce.presentation.facade;

import com.hhp.ecommerce.application.service.OrderService;
import com.hhp.ecommerce.application.service.ProductService;
import com.hhp.ecommerce.application.service.UserService;
import com.hhp.ecommerce.domain.model.Order;
import com.hhp.ecommerce.domain.model.OrderStatus;
import com.hhp.ecommerce.domain.model.Product;
import com.hhp.ecommerce.domain.model.User;
import com.hhp.ecommerce.infra.persistence.ProductRepository;
import com.hhp.ecommerce.infra.persistence.UserRepository;
import com.hhp.ecommerce.presentation.dto.OrderRequest;
import com.hhp.ecommerce.presentation.dto.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class OrderPaymentFacadeImplIntegrationTest {

    @Autowired
    private OrderPaymentFacade orderPaymentFacade;

    @Autowired
    private ProductService productService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderService orderService;

    private User user;
    private Product product;

    @BeforeEach
    public void setUp() {
        User saveUser = User.create("Alice", "alice@example.com", 1000);
        user = userRepository.save(saveUser);

        Product saveProduct = Product.create("Laptop", 1500, 100);
        product = productRepository.save(saveProduct);

    }


    @Test
    @Transactional
    public void testConcurrentOrderPaymentProcessing() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        OrderRequest orderRequest = OrderRequest.builder()
                .userId(1L)
                .items(List.of(
                        OrderRequest.OrderItem.builder()
                                .productId(101L)
                                .quantity(2)
                                .price(15000)
                                .build(),
                        OrderRequest.OrderItem.builder()
                                .productId(102L)
                                .quantity(1)
                                .price(20000)
                                .build()
                ))
                .build();

        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                PaymentResponse response = orderPaymentFacade.createOrderAndProcessPayment(orderRequest, "idempotencyKey-" + Thread.currentThread().getId());
                if (response.isPaymentSuccessful()) {
                    assertThat(response.getMessage()).isEqualTo("결제 및 주문 완료");
                } else {
                    assertThat(response.getMessage()).isEqualTo("결제 실패");
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        // 재고 및 주문 상태 검증
        Product updatedProduct = productService.getProductById(product.getId());
        assertThat(updatedProduct.getStock()).isLessThanOrEqualTo(0);

        List<Order> orders = orderService.getOrdersByUserId(user.getId());
        long successfulOrders = orders.stream().filter(order -> order.getOrderStatus().equals(OrderStatus.SUCCESS)).count();
        long failedOrders = orders.size() - successfulOrders;

        assertThat(successfulOrders).isGreaterThan(0);
        assertThat(failedOrders).isGreaterThan(0);
    }
}