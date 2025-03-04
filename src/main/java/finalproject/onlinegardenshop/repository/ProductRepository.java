package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {



}
