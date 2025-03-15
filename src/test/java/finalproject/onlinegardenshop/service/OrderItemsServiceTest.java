package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.OrderItemsDto;
import finalproject.onlinegardenshop.entity.OrderItems;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.OrderItemsMapper;
import finalproject.onlinegardenshop.repository.OrderItemsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderItemsService.
 */
@ExtendWith(MockitoExtension.class)
class OrderItemsServiceTest {

    @Mock
    private OrderItemsRepository repository;

    @Mock
    private OrderItemsMapper mapper;

    @InjectMocks
    private OrderItemsService service;

    private OrderItems orderItem;
    private OrderItemsDto orderItemDto;

    @BeforeEach
    void setUp() {
        orderItem = new OrderItems();
        orderItem.setId(1);

        orderItemDto = new OrderItemsDto();
        orderItemDto.setId(1);
    }

    /**
     * Test fetching all order items.
     */
    @Test
    void getAllOrderItems_ShouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(orderItem));
        when(mapper.toDto(any())).thenReturn(orderItemDto);

        List<OrderItemsDto> result = service.getAllOrderItems();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(orderItemDto.getId(), result.getFirst().getId());
    }

    /**
     * Test fetching an order item by ID when it exists.
     */
    @Test
    void getOrderItemById_ShouldReturnOrderItemDto_WhenExists() {
        when(repository.findById(1)).thenReturn(Optional.of(orderItem));
        when(mapper.toDto(any())).thenReturn(orderItemDto);

        OrderItemsDto result = service.getOrderItemById(1);

        assertNotNull(result);
        assertEquals(orderItemDto.getId(), result.getId());
    }

    /**
     * Test fetching an order item by ID when it does not exist.
     */
    @Test
    void getOrderItemById_ShouldThrowException_WhenNotExists() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(OnlineGardenShopResourceNotFoundException.class, () -> service.getOrderItemById(1));

        assertEquals("OrderItem not found with id 1", exception.getMessage());
    }
}
