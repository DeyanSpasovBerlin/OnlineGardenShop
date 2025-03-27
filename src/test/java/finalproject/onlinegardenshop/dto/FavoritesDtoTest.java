package finalproject.onlinegardenshop.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.*;

class FavoritesDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {

        validator = new LocalValidatorFactoryBean();
        ((LocalValidatorFactoryBean) validator).afterPropertiesSet();
    }

    @Test
    void testValidFavoritesDto() {

        FavoritesDto favoritesDto = new FavoritesDto(1, 1, "ACTIVE");

        BindingResult bindingResult = new org.springframework.validation.BeanPropertyBindingResult(favoritesDto, "favoritesDto");
        validator.validate(favoritesDto, bindingResult);

        assertFalse(bindingResult.hasErrors(), "Expected no validation errors, but found some.");
    }

    @Test
    void testInvalidFavoritesDtoWithNullProductId() {

        FavoritesDto favoritesDto = new FavoritesDto(1, null, "ACTIVE");

        BindingResult bindingResult = new org.springframework.validation.BeanPropertyBindingResult(favoritesDto, "favoritesDto");
        validator.validate(favoritesDto, bindingResult);

        assertNotNull(bindingResult.getFieldError("productsId"), "Expected validation error for 'productsId'.");
        assertEquals("{validation.favorites.productId}", bindingResult.getFieldError("productsId").getDefaultMessage());
    }
}
