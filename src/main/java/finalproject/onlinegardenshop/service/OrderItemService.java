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
    private final OrderItemMapper mapper;

    public OrderItemService(OrderItemRepository repository, OrderItemMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<OrderItemsDto> getAllOrderItems() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public OrderItemsDto getOrderItemById(Integer id) {
        return repository.findById(id).map(mapper::toDto).orElse(null);
    }

    public OrderItemsDto saveOrderItem(OrderItemsDto dto) {
        OrderItem entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }
}

