package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.OrderItemsDto;
import finalproject.onlinegardenshop.service.OrderItemsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderItemsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderItemsService service;

    @InjectMocks
    private OrderItemsController controller;

    private OrderItemsDto orderItemDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        orderItemDto = new OrderItemsDto();
        orderItemDto.setId(1);
    }

    @Test
    void getAllOrderItems_ShouldReturnOrderItemsList() throws Exception {
        when(service.getAllOrderItems()).thenReturn(List.of(orderItemDto));

        mockMvc.perform(get("/orders/1/order-items/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1));

        verify(service, times(1)).getAllOrderItems();
    }
}
