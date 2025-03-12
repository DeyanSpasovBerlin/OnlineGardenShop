package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemsRepository extends JpaRepository<CartItems,Integer> {
    Optional<CartItems> findByCartIdAndProductsId(Integer cartId, Integer productId);


    List<CartItems> findByCartId(Integer cartId);
    //    Optional<CartItem> findByCartAndProduct(Cart cart, Products product);
}
