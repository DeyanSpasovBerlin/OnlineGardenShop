package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.OrderItemsDto;
import finalproject.onlinegardenshop.entity.OrderItems;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.OrderItemsMapper;
import finalproject.onlinegardenshop.repository.OrderItemsRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemsService {
    private final OrderItemsRepository repository;
    private final OrderItemsMapper mapper;

    public OrderItemsService(OrderItemsRepository repository, @Qualifier("orderItemsMapperImpl") OrderItemsMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<OrderItemsDto> getOrderItemsForOrder(Integer orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<OrderItems> items = repository.findByOrderId(orderId).stream()
                .filter(item -> item.getOrder().getUser().getEmail().equals(email))
                .collect(Collectors.toList());

        if (items.isEmpty()) {
            throw new OnlineGardenShopResourceNotFoundException("No order items found for order " + orderId + " or access denied");
        }

        return items.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public OrderItemsDto getOrderItemById(Integer id) {
        OrderItems orderItem = repository.findById(id)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("OrderItem not found with id " + id));
        return mapper.toDto(orderItem);
    }
}
