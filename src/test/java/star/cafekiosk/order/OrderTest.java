package star.cafekiosk.order;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import star.cafekiosk.product.Product;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .plugin(new JakartaValidationPlugin()) // Jakarta Validation 적용
            .build();


    @DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void calculateTotalPrice() {
        // given
        var product1 = fixtureMonkey.giveMeBuilder(Product.class)
                .set("price", 1000)
                .set("productNumber", "001")
                .sample();
        var product2 = fixtureMonkey.giveMeBuilder(Product.class)
                .set("price", 2000)
                .set("productNumber", "002")
                .sample();


        // when
        var order = Order.create(List.of(product1, product2), LocalDateTime.now());


        // then
        assertThat(order.getTotalPrice()).isEqualTo(3000);

    }


    @DisplayName("주문 생성 시 상품 상품 상태는 INIT 이다.")
    @Test
    void orderStatusInit() {
        // given
        var product1 = fixtureMonkey.giveMeBuilder(Product.class)
                .set("price", 1000)
                .set("productNumber", "001")
                .sample();
        var product2 = fixtureMonkey.giveMeBuilder(Product.class)
                .set("price", 2000)
                .set("productNumber", "002")
                .sample();

        // when
        var order = Order.create(List.of(product1, product2), LocalDateTime.now());


        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.INIT);

    }


    @DisplayName("주문 생성 시 주문 등록 시간을 기록한다.")
    @Test
    void registeredDateTime() {
        // given
        var product1 = fixtureMonkey.giveMeBuilder(Product.class)
                .set("price", 1000)
                .set("productNumber", "001")
                .sample();
        var product2 = fixtureMonkey.giveMeBuilder(Product.class)
                .set("price", 2000)
                .set("productNumber", "002")
                .sample();

        var products = List.of(product1, product2);
        var registeredDateTime = LocalDateTime.now();

        // when
        Order order = Order.create(products, registeredDateTime);

        // then
        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
    }

}