package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Categories;
import finalproject.onlinegardenshop.entity.Products;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.liquibase.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ProductsRepositoryTest {

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void updateCategoryToNull_shouldSetProductCategoryToNull() {
        // Arrange
        Categories category = new Categories();
        category.setName("Electronics");
        entityManager.persist(category);

        Products product = new Products();
        product.setName("Laptop");
        product.setPrice(1500.0);
        product.setDiscountPrice(1200.0);
        product.setCategory(category);
        entityManager.persist(product);

        entityManager.flush();

        // Act
        productsRepository.updateCategoryToNull(category.getId());
        entityManager.clear(); // Очистить контекст, чтобы заново загрузить entity

        // Assert
        Products updatedProduct = entityManager.find(Products.class, product.getId());
        assertNotNull(updatedProduct);
        assertNull(updatedProduct.getCategory(), "Category should be null after update");
    }

    @Test
    void findBestDiscountedProduct_shouldReturnProductWithHighestDiscountAmount() {
        // Arrange
        Products p1 = new Products();
        p1.setName("Phone");
        p1.setPrice(800.0);
        p1.setDiscountPrice(700.0);
        entityManager.persist(p1);

        Products p2 = new Products();
        p2.setName("TV");
        p2.setPrice(2000.0);
        p2.setDiscountPrice(1000.0); // Наибольшая скидка
        entityManager.persist(p2);

        Products p3 = new Products();
        p3.setName("Headphones");
        p3.setPrice(300.0);
        p3.setDiscountPrice(250.0);
        entityManager.persist(p3);

        entityManager.flush();

        // Act
        Optional<Products> result = productsRepository.findBestDiscountedProduct();

        // Assert
        assertTrue(result.isPresent());
        assertEquals("TV", result.get().getName(), "Should return product with highest discount");
    }
}
