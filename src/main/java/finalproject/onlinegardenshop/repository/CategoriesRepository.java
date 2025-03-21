package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Integer> {

    Optional<Categories> findByName(String name);

}
