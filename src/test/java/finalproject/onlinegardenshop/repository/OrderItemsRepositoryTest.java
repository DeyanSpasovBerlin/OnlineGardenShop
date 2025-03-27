package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.OrderItems;
import finalproject.onlinegardenshop.entity.Products;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderItemsRepositoryTest {

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void updateProductToNull_ShouldSetProductToNull_WhenProductIdIsProvided() {

        Products product = new Products();
        product.setName("Test Product");
        product.setPrice(100.0);
        testEntityManager.persistAndFlush(product);

        OrderItems orderItem = new OrderItems();
        orderItem.setProduct(product);
        testEntityManager.persistAndFlush(orderItem);

        orderItemsRepository.updateProductToNull(product.getId());

        testEntityManager.clear();

        OrderItems updatedOrderItem = testEntityManager.find(OrderItems.class, orderItem.getId());
        assertNull(updatedOrderItem.getProduct());
    }

    @Test
    void updateProductToNull_ShouldNotAffectOtherOrderItems() {

        Products products1 = new Products();
        products1.setName("Test Product 1");
        products1.setPrice(100.0);
        testEntityManager.persistAndFlush(products1);

        Products product2 = new Products();
        product2.setName("Test Product 2");
        product2.setPrice(200.0);
        testEntityManager.persistAndFlush(product2);

        OrderItems orderItem1 = new OrderItems();
        orderItem1.setProduct(products1);
        testEntityManager.persistAndFlush(orderItem1);

        OrderItems orderItem2 = new OrderItems();
        orderItem2.setProduct(product2);
        testEntityManager.persistAndFlush(orderItem2);

        orderItemsRepository.updateProductToNull(products1.getId());

        testEntityManager.clear();

        OrderItems updatedOrderItem1 = testEntityManager.find(OrderItems.class, orderItem1.getId());
        OrderItems updatedOrderItem2 = testEntityManager.find(OrderItems.class, orderItem2.getId());

        assertNull(updatedOrderItem1.getProduct());
        assertNotNull(updatedOrderItem2.getProduct());
        assertEquals(product2.getId(), updatedOrderItem2.getProduct().getId());
    }
}