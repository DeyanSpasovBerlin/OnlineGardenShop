package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Favorites;
import finalproject.onlinegardenshop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites, Integer> {

    List<Favorites> findByProductId(Integer productId);
    List<Favorites> findByUserId(Integer userId);

    List<Favorites> findAllByUser(Users user);

}
