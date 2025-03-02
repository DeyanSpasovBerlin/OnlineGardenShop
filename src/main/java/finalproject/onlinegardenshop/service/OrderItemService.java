package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.OrderItemsDto;
import finalproject.onlinegardenshop.entity.OrderItem;
import finalproject.onlinegardenshop.repository.OrderItemRepository;
import finalproject.onlinegardenshop.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    private final OrderItemRepository repository;

    public OrderItemService(OrderItemRepository repository) {
        this.repository = repository;
    }

    public List<OrderItemsDto> getAllOrderItems() {
        return repository.findAll().stream().map(OrderItemMapper::toDto).collect(Collectors.toList());
    }

    public OrderItemsDto getOrderItemById(Integer id) {
        return repository.findById(id).map(OrderItemMapper::toDto).orElse(null);
    }

    public OrderItemsDto saveOrderItem(OrderItemsDto dto) {
        OrderItem entity = OrderItemMapper.toEntity(dto);
        return OrderItemMapper.toDto(repository.save(entity));
    }
}

