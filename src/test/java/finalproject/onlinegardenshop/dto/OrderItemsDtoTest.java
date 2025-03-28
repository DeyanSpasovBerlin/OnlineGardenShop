package finalproject.onlinegardenshop.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemsDtoTest {

    @Test
    void shouldCreateOrderItemsDtoCorrectly() {
        OrderItemsDto dto = new OrderItemsDto(1, 10, 20, 2, 15.99);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals(10, dto.getOrderId());
        assertEquals(20, dto.getProductId());
        assertEquals(2, dto.getQuantity());
        assertEquals(15.99, dto.getPriceAtPurchase(), 0.001);
    }

    @Test
    void shouldAllowSettingValues() {
        OrderItemsDto dto = new OrderItemsDto();
        dto.setId(2);
        dto.setOrderId(11);
        dto.setProductId(21);
        dto.setQuantity(3);
        dto.setPriceAtPurchase(25.50);

        assertEquals(2, dto.getId());
        assertEquals(11, dto.getOrderId());
        assertEquals(21, dto.getProductId());
        assertEquals(3, dto.getQuantity());
        assertEquals(25.50, dto.getPriceAtPurchase(), 0.001);
    }
}
