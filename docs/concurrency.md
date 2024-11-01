# 동시성 제어 방식 분석
분석 적용 시나리오 : 주문 결제
[OrderPaymentFacadeImpl.java](..%2Fsrc%2Fmain%2Fjava%2Fcom%2Fhhp%2Fecommerce%2Fpresentation%2Ffacade%2FOrderPaymentFacadeImpl.java)

### 1.주요 동시성 문제
1. 재고 확인 및 감소 (ProductService)
    - 여러 사용자가 동일한 상품을 동시에 주문하는 경우, 재고의 명확성이 떨어진다.
    -  사용자가 `productService.checkStockAvailability`로 재고를 확인하고, `reduceStock`으로 재고를 감소시키기 전에 다른 사용자가 동일한 재고를 가져갈 수 있는 상황이 발생할 수 있다.
        
2. 주문 생성 및 상태 업데이트 (OrderService)
    - 주문 생성 및 주문 상태를 업데이트 하는 동안 다른 트랜젝션이 해당 주문에 접근하거나 주문 연관 데이터를 갱신 할 수 있다.
    - 결제 실패, 재고 부족으로 인해 취소되는 경우에는 데이터를 일관하게 유지하는게 중요하다.
---
### 2. 동시성 제어 방식과 적용된 코드
**A. 비관적 잠금 (Pessimistic Locking)**
- [ProductRepository.java](..%2Fsrc%2Fmain%2Fjava%2Fcom%2Fhhp%2Fecommerce%2Finfra%2Fpersistence%2FProductRepository.java) 와 [OrderRepository.java](..%2Fsrc%2Fmain%2Fjava%2Fcom%2Fhhp%2Fecommerce%2Finfra%2Fpersistence%2FOrderRepository.java) 에서 적용
```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("select p from Product p where p.id = :productId")
Optional<Product> findByIdWithLock(@Param("productId") Long productId);
```
- 장점
   - 데이터 일관성을 보장하며, 충돌 발생이 쉬운 데이터를 안전하게 보호 할 수 있다.
-  단점
   - 잠금이 걸린 동안 다른 트랜젝션이 대기 상태에 들어가므로 성능 저하될 수 있다.
   - 높은 동시성이 필요한 경우에는 효율이 떨어질 수 있다.  

**B. 트랙잭션 범위에서 비관적 잠금 사용**
- [ProductService.java](..%2Fsrc%2Fmain%2Fjava%2Fcom%2Fhhp%2Fecommerce%2Fapplication%2Fservice%2FProductService.java) `reduceStock`과 `checkStockAvailability` 메서드는 `findByIdWithLock`을 사용하여 재고 확인 및 감소 시 비관적 잠금을 적용
- [OrderService.java](..%2Fsrc%2Fmain%2Fjava%2Fcom%2Fhhp%2Fecommerce%2Fapplication%2Fservice%2FOrderService.java)에서도 동일한 방식으로 잠금을 통해 데이터 충돌을 방지
```java
@Transactional
public void reduceStock(Long productId, int quantity) {
    Optional<Product> productOptional = productRepository.findByIdWithLock(productId);
    Product product = productOptional.orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));
    product.reduceStock(quantity);
    productRepository.save(product);
}
```
- 장점:
   - 트랜잭션 단위에서 데이터 일관성을 유지할 수 있어 재고가 잘못 관리되는 문제를 방지할 수 있다.
- 단점:
   - 트랜잭션이 길어질 경우 다른 트랜잭션이 대기해야 하는 상황이 발생할 수 있다.

**C. 추가적인 동시성 제어 옵션: 분산 락**
- Redis와 같은 분산 시스템을 통해 글로벌 락을 구현하는 방법도 고려할 수 있다.
- 여러 애플리케이션 인스턴스가 있는 경우 분산 락으로 글로벌한 동시성 문제를 해결할 수 있다.