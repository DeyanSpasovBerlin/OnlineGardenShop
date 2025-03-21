package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.OrderItemsDto;
import finalproject.onlinegardenshop.entity.OrderItems;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.entity.Products;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OrderItemsMapperTest {

    private OrderItemsMapper orderItemsMapper;

    @BeforeEach
    public void setUp() {
        orderItemsMapper = OrderItemsMapper.INSTANCE;
    }

    @Test
    public void testToDto() {

        Orders order = new Orders();
        order.setId(1);

        Products product = new Products();
        product.setId(2);

        OrderItems orderItems = new OrderItems();
        orderItems.setOrder(order);
        orderItems.setProduct(product);

        OrderItemsDto orderItemsDto = orderItemsMapper.toDto(orderItems);

        assertEquals(orderItems.getOrder().getId(), orderItemsDto.getOrderId());
        assertEquals(orderItems.getProduct().getId(), orderItemsDto.getProductId());
    }

    @Test
    public void testToEntity() {
        // Создаем DTO для тестирования
        OrderItemsDto orderItemsDto = new OrderItemsDto();
        orderItemsDto.setOrderId(1);
        orderItemsDto.setProductId(2);

        OrderItems orderItems = orderItemsMapper.toEntity(orderItemsDto);

        assertNull(orderItems.getOrder());
        assertNull(orderItems.getProduct());
    }
}
