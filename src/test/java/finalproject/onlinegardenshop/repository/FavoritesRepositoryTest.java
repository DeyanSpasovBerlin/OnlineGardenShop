package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Favorites;
import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FavoritesRepositoryTest {

    @Autowired
    private FavoritesRepository favoritesRepository;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private ProductsRepository productRepository;

    @Test
    void testFindByUserId() {
        Users user = new Users();
        user.setName("Test User");
        user = userRepository.save(user);

        Products product = new Products();
        product.setName("Test Product");
        product = productRepository.save(product);

        Favorites favorite = new Favorites();
        favorite.setUser(user);
        favorite.setProduct(product);
        favoritesRepository.save(favorite);

        List<Favorites> favoritesList = favoritesRepository.findByUserId(user.getId());
        assertThat(favoritesList).hasSize(1);
        assertThat(favoritesList.get(0).getProduct().getId()).isEqualTo(product.getId());
    }

    @Test
    void testFindByProductId() {

        Users user = new Users();
        user.setName("Test User");
        user = userRepository.save(user);

        Products product = new Products();
        product.setName("Test Product");
        product = productRepository.save(product);

        Favorites favorite = new Favorites();
        favorite.setUser(user);
        favorite.setProduct(product);
        favoritesRepository.save(favorite);

        List<Favorites> favoritesList = favoritesRepository.findByProductId(product.getId());
        assertThat(favoritesList).hasSize(1);
        assertThat(favoritesList.get(0).getUser().getId()).isEqualTo(user.getId());
    }
}
