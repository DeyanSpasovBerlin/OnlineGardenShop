package finalproject.onlinegardenshop.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderItemsTest {

    @Test
    void shouldCreateOrderItemsEntityCorrectly() {
        Orders order = new Orders();
        Products product = new Products();

        OrderItems orderItem = new OrderItems(1, order, product, 5, 19.99);

        assertNotNull(orderItem);
        assertEquals(1, orderItem.getId());
        assertEquals(order, orderItem.getOrder());
        assertEquals(product, orderItem.getProduct());
        assertEquals(5, orderItem.getQuantity());
        assertEquals(19.99, orderItem.getPriceAtPurchase(), 0.001);
    }

    @Test
    void shouldAllowSettingValues() {
        Orders order = new Orders();
        Products product = new Products();

        OrderItems orderItem = new OrderItems();
        orderItem.setId(2);
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(10);
        orderItem.setPriceAtPurchase(29.99);

        assertEquals(2, orderItem.getId());
        assertEquals(order, orderItem.getOrder());
        assertEquals(product, orderItem.getProduct());
        assertEquals(10, orderItem.getQuantity());
        assertEquals(29.99, orderItem.getPriceAtPurchase(), 0.001);
    }
}
