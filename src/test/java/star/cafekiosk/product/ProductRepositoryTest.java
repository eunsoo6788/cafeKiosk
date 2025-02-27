package star.cafekiosk.product;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .plugin(new JakartaValidationPlugin()) // Jakarta Validation 적용
            .build();



    @Test
    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    void findAllBySellingStatusIn() {

        // given
        Product product1 = fixtureMonkey.giveMeBuilder(Product.class)
                .setNull("id")
                .set("sellingStatus", ProductSellingStatus.SELLING)
                .sample();

        Product product2 = fixtureMonkey.giveMeBuilder(Product.class)
                .setNull("id")
                .set("sellingStatus", ProductSellingStatus.HOLD)
                .sample();

        Product product3 = fixtureMonkey.giveMeBuilder(Product.class)
                .setNull("id")
                .set("sellingStatus", ProductSellingStatus.STOP_SELLING)
                .sample();

        repository.saveAll(List.of(product1, product2, product3));

        // when
        var savedProducts = repository.findAllBySellingStatusIn(List.of(ProductSellingStatus.SELLING, ProductSellingStatus.HOLD));

        // then
        assertThat(savedProducts)
                .hasSize(2)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id") // id 필드는 제외하고 값이 동일한지 비교한다.
                .containsExactlyInAnyOrderElementsOf(List.of(product1, product2));

    }


    @DisplayName("상품번호 리스트로 상품들을 조회한다.")
    @Test
    void findAllByProductNumberIn() {

        // given
        var products = fixtureMonkey.giveMeBuilder(Product.class)
                .setNull("id")
                .sampleList(3);

        repository.saveAll(products);

        var productNumbers = products
                .stream()
                .limit(2)
                .map(Product::getProductNumber)
                .toList();

        // when
        var savedProducts = repository.findAllByProductNumberIn(productNumbers);

        // then
        assertThat(savedProducts).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple(products.get(0).getProductNumber(), products.get(0).getName(), products.get(0).getSellingStatus()),
                        tuple(products.get(1).getProductNumber(), products.get(1).getName(), products.get(1).getSellingStatus())
                );


    }
}