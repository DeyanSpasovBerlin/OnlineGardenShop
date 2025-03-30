package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Products;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer> {

    // Фильтрация по категории и цене
    List<Products> findByCategory_Name(String category, Sort sort);

    List<Products> findByCategory_NameAndPriceBetween(String category, Double minPrice, Double maxPrice, Sort sort);

    List<Products> findByCategory_NameAndPriceBetweenAndDiscountPriceGreaterThan(
            String category, Double minPrice, Double maxPrice, Double discountPrice, Sort sort);

    List<Products> findByCategory_NameAndPriceBetweenAndDiscountPriceIsNull(
            String category, Double minPrice, Double maxPrice, Sort sort);

    // Фильтрация по цене
    List<Products> findByPriceBetween(Double minPrice, Double maxPrice, Sort sort);

    // Фильтрация по скидке
    List<Products> findByDiscountPriceGreaterThan(Double discountPrice, Sort sort);

    List<Products> findByDiscountPriceIsNull(Sort sort);

    List<Products> findByCategory_NameAndDiscountPriceGreaterThan(String category, Double discountPrice, Sort sort);

    List<Products> findByCategory_NameAndDiscountPriceIsNull(String category, Sort sort);

    @Modifying
    @Query("UPDATE Products p SET p.category = NULL WHERE p.category.id = :categoryId")
    void updateCategoryToNull(@Param("categoryId") Integer categoryId);

    // Проверка существования товара по ID
    boolean existsById(Integer id);

    @Query(value = "SELECT * FROM products WHERE discount_price > 0 ORDER BY (price - discount_price) DESC, RAND() LIMIT 1", nativeQuery = true)
    Optional<Products> findBestDiscountedProduct();

}

