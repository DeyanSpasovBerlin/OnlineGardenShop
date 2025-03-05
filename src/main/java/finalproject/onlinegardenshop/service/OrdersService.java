package finalproject.onlinegardenshop.service;


import finalproject.onlinegardenshop.dto.OrdersDto;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.OrdersMapper;
import finalproject.onlinegardenshop.repository.OrdersRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrdersService {

    private static Logger logger = LogManager.getLogger(OrdersService.class);

    private final OrdersMapper ordersMapper;
    private final OrdersRepository ordersRepository;

    @Autowired
    public OrdersService(OrdersMapper ordersMapper, OrdersRepository ordersRepository) {
        this.ordersMapper = ordersMapper;
        this.ordersRepository = ordersRepository;
    }

    public List<OrdersDto> getAll(){
        List<Orders> orders = ordersRepository.findAll();
        logger.debug("Orders retrieved from db");
        logger.debug("Orders ids: {}", () -> orders.stream().map(Orders::getId).toList());
        return ordersMapper.entityListToDto(orders);
    }

    public OrdersDto getOrderssById(Integer id) {
        Optional<Orders> optional = ordersRepository.findById(id);
        if (optional.isPresent()) {
            OrdersDto found = ordersMapper.entityToDto(optional.get()) ;
            return found;
        }
        throw new OnlineGardenShopResourceNotFoundException("Orders with id = " + id + " not found in database");
    }
}
