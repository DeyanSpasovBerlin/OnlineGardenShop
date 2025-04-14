package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Favorites;
import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritesRepository extends JpaRepository<Favorites, Integer> {

    List<Favorites> findByProductId(Integer productId);
    List<Favorites> findByUserId(Integer userId);

    List<Favorites> findAllByUser(Users user);

    Optional<Object> findByUserAndProduct(Users currentUser, Products product);
}
