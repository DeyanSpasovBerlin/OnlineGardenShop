//package finalproject.onlinegardenshop.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import finalproject.onlinegardenshop.dto.OrderItemsDto;
//import finalproject.onlinegardenshop.service.OrderItemsService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = OrderItemsController.class)
//class OrderItemsControllerTest {
//
//    @MockitoBean
//    private OrderItemsService service;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper mapper;

//    @Test
//    void getAllOrderItems() throws Exception {
//        Integer orderId = 1;
//        List<OrderItemsDto> orderItems = Collections.emptyList();
//        when(service.getAllOrderItems()).thenReturn(orderItems);
//
//        mockMvc.perform(get("/orders/" + orderId + "/order-items/all")
//                        .contentType("application/json"))
//                .andExpect(status().isOk());
//
//        verify(service).getAllOrderItems();
//    }
//}
