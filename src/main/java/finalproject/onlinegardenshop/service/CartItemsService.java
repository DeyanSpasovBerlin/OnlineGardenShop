//package finalproject.onlinegardenshop.service;
//
//import finalproject.onlinegardenshop.entity.CartItems;
//import finalproject.onlinegardenshop.mapper.CartItemsMapper;
//import finalproject.onlinegardenshop.repository.CartItemsRepository;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import finalproject.onlinegardenshop.dto.CartItemstDto;
//
//
//import java.util.List;
//
//@Service
//@Transactional(readOnly = true)
//public class CartItemsService {
//
//    private static Logger logger = LogManager.getLogger(CartItemsService.class);
//
//    private final CartItemsMapper cartItemsMapper;
//    private final CartItemsRepository cartItemsRepository;
//
//    @Autowired
//    public CartItemsService(CartItemsMapper cartItemsMapper, CartItemsRepository cartItemsRepository) {
//        this.cartItemsMapper = cartItemsMapper;
//        this.cartItemsRepository = cartItemsRepository;
//    }
//
//
//
//
//    public List<CartItemstDto> getAll(){
//        List<CartItems> cartItems = cartItemsRepository.findAll();
//        logger.debug("CartItems retrieved from db");
//        logger.debug("CartItems ids: {}", () -> cartItems.stream().map(CartItems::getId).toList());
//        return cartItemsMapper.entityListToDto(cartItems);
//    }
//
//
//}
