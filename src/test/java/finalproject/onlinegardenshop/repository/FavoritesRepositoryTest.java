package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Favorites;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class FavoritesRepositoryTest {

    @Mock
    private FavoritesRepository favoritesRepository;

    @Test
    void testFindById() {

        Favorites favorite = new Favorites();
        favorite.setId(1);

        Mockito.when(favoritesRepository.findById(1))
                .thenReturn(Optional.of(favorite));

        Optional<Favorites> foundFavorite = favoritesRepository.findById(1);

        assertTrue(foundFavorite.isPresent());
        assertEquals(1, foundFavorite.get().getId());

        Mockito.verify(favoritesRepository, Mockito.times(1)).findById(1);
    }
}
