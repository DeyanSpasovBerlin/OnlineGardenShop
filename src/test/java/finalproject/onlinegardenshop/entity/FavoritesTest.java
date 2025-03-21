package finalproject.onlinegardenshop.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

class FavoritesTest {

    @Test
    void testFavoritesEntity() {

        Users mockUser = Mockito.mock(Users.class);
        Products mockProduct = Mockito.mock(Products.class);

        Favorites favorite = new Favorites();
        favorite.setId(1);
        favorite.setUser(mockUser);
        favorite.setProduct(mockProduct);
        favorite.setTestData("Sample Data");

        assertEquals(1, favorite.getId());
        assertEquals(mockUser, favorite.getUser());
        assertEquals(mockProduct, favorite.getProduct());
        assertEquals("Sample Data", favorite.getTestData());
    }
}
