package finalproject.onlinegardenshop.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FavoritesDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenProductsIdIsNull_ShouldReturnConstraintViolation() {
        FavoritesDto dto = new FavoritesDto();
        dto.setId(1);
        dto.setProductsId(null);

        Set<ConstraintViolation<FavoritesDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("{validation.favorites.productId}", violations.iterator().next().getMessage());
    }

    @Test
    void whenProductsIdIsNotNull_ShouldPassValidation() {
        FavoritesDto dto = new FavoritesDto(1, 100);

        Set<ConstraintViolation<FavoritesDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}
