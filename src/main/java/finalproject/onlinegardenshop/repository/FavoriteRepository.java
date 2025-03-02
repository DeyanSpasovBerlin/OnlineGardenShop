package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
}
