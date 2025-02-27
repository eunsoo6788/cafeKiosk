package star.cafekiosk.api.service;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import star.cafekiosk.api.controller.dto.OrderCreateRequest;
import star.cafekiosk.order.OrderRepository;
import star.cafekiosk.orderproduct.OrderProductRepository;
import star.cafekiosk.product.Product;
import star.cafekiosk.product.ProductRepository;
import star.cafekiosk.product.ProductSellingStatus;
import star.cafekiosk.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderService orderService;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
    }


    private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .plugin(new JakartaValidationPlugin()) // Jakarta Validation 적용
            .build();


    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void create() {

        // given
        var product1 = fixtureMonkey.giveMeBuilder(Product.class)
                .setNull("id")
                .set("type", ProductType.HANDMADE)
                .set("sellingStatus", ProductSellingStatus.SELLING)
                .set("productNumber", "001")
                .set("price", 1000)
                .sample();
        var product2 = fixtureMonkey.giveMeBuilder(Product.class)
                .setNull("id")
                .set("type", ProductType.HANDMADE)
                .set("sellingStatus", ProductSellingStatus.SELLING)
                .set("productNumber", "002")
                .set("price", 3000)
                .sample();
        var product3 = fixtureMonkey.giveMeBuilder(Product.class)
                .setNull("id")
                .set("type", ProductType.HANDMADE)
                .set("sellingStatus", ProductSellingStatus.SELLING)
                .set("productNumber", "003")
                .set("price", 5000)
                .sample();

        productRepository.saveAll(List.of(product1, product2, product3));


        var request = OrderCreateRequest.builder()
                .productNumbers(List.of("001","002"))
                .build();

        var registerdDateTime = LocalDateTime.now();

        // when
        var response = orderService.create(request, registerdDateTime);


        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response)
                .extracting("registeredDateTime","totalPrice")
                .contains(registerdDateTime, 4000);

    }


    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithDuplicateProductNumbers() {
        // given
        var registeredDateTime = LocalDateTime.now();

        var product1 = fixtureMonkey.giveMeBuilder(Product.class)
                .setNull("id")
                .set("type", ProductType.HANDMADE)
                .set("sellingStatus", ProductSellingStatus.SELLING)
                .set("productNumber", "001")
                .set("price", 1000)
                .sample();
        var product2 = fixtureMonkey.giveMeBuilder(Product.class)
                .setNull("id")
                .set("type", ProductType.HANDMADE)
                .set("sellingStatus", ProductSellingStatus.SELLING)
                .set("productNumber", "002")
                .set("price", 3000)
                .sample();
        var product3 = fixtureMonkey.giveMeBuilder(Product.class)
                .setNull("id")
                .set("type", ProductType.HANDMADE)
                .set("sellingStatus", ProductSellingStatus.SELLING)
                .set("productNumber", "003")
                .set("price", 5000)
                .sample();

        productRepository.saveAll(List.of(product1, product2, product3));

        var request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001"))
                .build();

        // when
        var response = orderService.create(request, registeredDateTime);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 2000);
        assertThat(response.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000)
                );
    }
}