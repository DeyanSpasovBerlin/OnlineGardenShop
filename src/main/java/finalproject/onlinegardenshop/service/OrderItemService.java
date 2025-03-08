package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.OrderItemsDto;
import finalproject.onlinegardenshop.entity.OrderItem;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.repository.OrderItemRepository;
import finalproject.onlinegardenshop.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    private final OrderItemRepository repository;
    private final OrderItemMapper mapper;

    public OrderItemService(OrderItemRepository repository, OrderItemMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<OrderItemsDto> getAllOrderItems() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public OrderItemsDto getOrderItemById(Integer id) {
        OrderItem orderItem = repository.findById(id)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("OrderItem not found with id " + id));
        return mapper.toDto(orderItem);
    }
}