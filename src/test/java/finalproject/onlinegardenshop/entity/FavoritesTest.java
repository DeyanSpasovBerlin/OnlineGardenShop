package finalproject.onlinegardenshop.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FavoritesTest {

    @Test
    void shouldCreateFavoritesEntityCorrectly() {
        Users user = new Users();
        Products product = new Products();

        Favorites favorite = new Favorites(1, user, product);

        assertNotNull(favorite);
        assertEquals(1, favorite.getId());
        assertEquals(user, favorite.getUser());
        assertEquals(product, favorite.getProduct());
    }

    @Test
    void shouldAllowSettingValues() {
        Users user = new Users();
        Products product = new Products();

        Favorites favorite = new Favorites();
        favorite.setId(2);
        favorite.setUser(user);
        favorite.setProduct(product);

        assertEquals(2, favorite.getId());
        assertEquals(user, favorite.getUser());
        assertEquals(product, favorite.getProduct());
    }
}
