package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.FavoritesDto;
import finalproject.onlinegardenshop.entity.Favorites;
import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FavoritesMapperTest {

    private FavoritesMapper favoritesMapper;

    @BeforeEach
    public void setUp() {
        favoritesMapper = org.mapstruct.factory.Mappers.getMapper(FavoritesMapper.class);
    }

    @Test
    public void testToDto() {
        // Создаем объекты для тестирования
        Products product = new Products();
        product.setId(1); //L

        Users user = new Users();  // Это поле будет игнорироваться в тестах
        user.setId(1); // L

        Favorites favorite = new Favorites();
        favorite.setProduct(product);
        favorite.setUser(user);

        // Маппим объект Favorites в FavoritesDto
        FavoritesDto favoritesDto = favoritesMapper.toDto(favorite);

        // Проверяем, что маппинг работает корректно
        assertEquals(favorite.getProduct().getId(), favoritesDto.getProductsId());
    }

    @Test
    public void testToEntity() {
        // Создаем DTO для тестирования
        FavoritesDto favoritesDto = new FavoritesDto();
        favoritesDto.setProductsId(1); //L

        // Маппим DTO в сущность Favorites
        Favorites favorite = favoritesMapper.toEntity(favoritesDto);

        // Проверяем, что поля "users" и "products" не инициализируются, так как они игнорируются в маппере
        assertEquals(null, favorite.getUser());
        assertEquals(null, favorite.getProduct());
    }
}
