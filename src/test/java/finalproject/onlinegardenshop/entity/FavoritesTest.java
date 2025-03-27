package finalproject.onlinegardenshop.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class FavoritesTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Rollback
    void testFavoritesEntityPersistence() {
        Users user = new Users();
        entityManager.persist(user);

        Products product = new Products();
        entityManager.persist(product);

        Favorites favorite = new Favorites();
        favorite.setUser(user);
        favorite.setProduct(product);
        entityManager.persist(favorite);
        entityManager.flush();

        Favorites foundFavorite = entityManager.find(Favorites.class, favorite.getId());

        assertThat(foundFavorite).isNotNull();
        assertThat(foundFavorite.getUser()).isEqualTo(user);
        assertThat(foundFavorite.getProduct()).isEqualTo(product);
    }
}