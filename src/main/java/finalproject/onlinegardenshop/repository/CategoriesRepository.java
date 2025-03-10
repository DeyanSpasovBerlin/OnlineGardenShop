package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Integer> {



}
