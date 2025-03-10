package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.OrderItemsDto;
import finalproject.onlinegardenshop.entity.OrderItems;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.repository.OrderItemsRepository;
import finalproject.onlinegardenshop.mapper.OrderItemsMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemsService {
    private final OrderItemsRepository repository;
    private final OrderItemsMapper mapper;

    public OrderItemsService(OrderItemsRepository repository, OrderItemsMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<OrderItemsDto> getAllOrderItems() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public OrderItemsDto getOrderItemById(Integer id) {
        OrderItems orderItem = repository.findById(id)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("OrderItem not found with id " + id));
        return mapper.toDto(orderItem);
    }
}