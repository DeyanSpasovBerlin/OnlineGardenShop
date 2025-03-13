package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemsRepository extends JpaRepository<CartItems,Integer> {
    Optional<CartItems> findByCartIdAndProductsId(Integer cartId, Integer productId);

    List<CartItems> findByCartId(Integer cartId);
    //Optional<CartItem> findByCartAndProduct(Cart cart, Products product);//для показание имя продукта в Cart and Orders

    @Modifying
    @Query("DELETE FROM CartItems c WHERE c.cart.id = :cartId")
    void deleteByCartId(@Param("cartId") Integer cartId);//✅-> delete user!
}
