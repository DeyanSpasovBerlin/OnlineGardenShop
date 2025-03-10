package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritesRepository extends JpaRepository<Favorites, Integer> {
}
