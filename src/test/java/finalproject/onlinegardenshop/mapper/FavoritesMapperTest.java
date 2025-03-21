package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.FavoritesDto;
import finalproject.onlinegardenshop.entity.Favorites;
import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FavoritesMapperTest {

    private FavoritesMapper favoritesMapper;

    @BeforeEach
    public void setUp() {
        favoritesMapper = org.mapstruct.factory.Mappers.getMapper(FavoritesMapper.class);
    }

    @Test
    public void testToDto() {

        Products product = new Products();
        product.setId(1);

        Users user = new Users();
        user.setId(1);

        Favorites favorite = new Favorites();
        favorite.setProduct(product);
        favorite.setUser(user);

        FavoritesDto favoritesDto = favoritesMapper.toDto(favorite);

        assertEquals(favorite.getProduct().getId(), favoritesDto.getProductsId());
    }

    @Test
    public void testToEntity() {

        FavoritesDto favoritesDto = new FavoritesDto();
        favoritesDto.setProductsId(1);

        Favorites favorite = favoritesMapper.toEntity(favoritesDto);

        assertNull(favorite.getUser());
        assertNull(favorite.getProduct());
    }
}
