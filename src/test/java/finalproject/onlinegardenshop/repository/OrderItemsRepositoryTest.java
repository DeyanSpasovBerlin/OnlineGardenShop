package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.OrderItems;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemsRepositoryTest {

    @Mock
    private OrderItemsRepository orderItemsRepository;

    @Test
    void testFindByOrderId() {

        Integer orderId = 1;
        OrderItems item1 = new OrderItems();
        OrderItems item2 = new OrderItems();

        List<OrderItems> mockOrderItems = Arrays.asList(item1, item2);

        when(orderItemsRepository.findByOrderId(orderId)).thenReturn(mockOrderItems);

        List<OrderItems> foundItems = orderItemsRepository.findByOrderId(orderId);

        assertEquals(2, foundItems.size());

        verify(orderItemsRepository, times(1)).findByOrderId(orderId);
    }
}