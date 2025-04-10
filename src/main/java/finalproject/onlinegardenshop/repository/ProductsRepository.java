package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer>, JpaSpecificationExecutor<Products> {

    @Modifying
    @Query("UPDATE Products p SET p.category = NULL WHERE p.category.id = :categoryId")
    void updateCategoryToNull(@Param("categoryId") Integer categoryId);

    boolean existsById(Integer id);

    @Query(value = "SELECT * FROM products WHERE discount_price > 0 ORDER BY (price - discount_price) DESC, RAND() LIMIT 1", nativeQuery = true)
    Optional<Products> findBestDiscountedProduct();
}
