package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.OrderItemsDto;
import finalproject.onlinegardenshop.entity.OrderItems;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.entity.Products;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderItemsMapperTest {

    private OrderItemsMapper orderItemsMapper;

    @BeforeEach
    public void setUp() {
        orderItemsMapper = OrderItemsMapper.INSTANCE;
    }

    @Test
    public void testToDto() {
        // Создаем объекты для тестирования
        Orders order = new Orders();
        order.setId(1);

        Products product = new Products();
        product.setId(2);

        OrderItems orderItems = new OrderItems();
        orderItems.setOrder(order);
        orderItems.setProduct(product);

        // Маппим объект OrderItems в OrderItemsDto
        OrderItemsDto orderItemsDto = orderItemsMapper.toDto(orderItems);

        // Проверяем, что маппинг работает корректно
        assertEquals(orderItems.getOrder().getId(), orderItemsDto.getOrderId());
        assertEquals(orderItems.getProduct().getId(), orderItemsDto.getProductId());
    }

    @Test
    public void testToEntity() {
        // Создаем DTO для тестирования
        OrderItemsDto orderItemsDto = new OrderItemsDto();
        orderItemsDto.setOrderId(1);      //L
        orderItemsDto.setProductId(2);    // L

        // Маппим DTO в сущность OrderItems
        OrderItems orderItems = orderItemsMapper.toEntity(orderItemsDto);

        // Проверяем, что Order и Product не инициализируются, так как они игнорируются в маппере
        assertEquals(null, orderItems.getOrder());
        assertEquals(null, orderItems.getProduct());
    }
}
