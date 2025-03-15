package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.FavoritesDto;
import finalproject.onlinegardenshop.entity.Favorites;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.FavoritesMapper;
import finalproject.onlinegardenshop.repository.FavoritesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FavoritesService.
 */
@ExtendWith(MockitoExtension.class)
class FavoritesServiceTest {

    @Mock
    private FavoritesRepository repository;

    @Mock
    private FavoritesMapper mapper;

    @InjectMocks
    private FavoritesService service;

    private Favorites favorite;
    private FavoritesDto favoriteDto;

    @BeforeEach
    void setUp() {
        favorite = new Favorites();
        favorite.setId(1);

        favoriteDto = new FavoritesDto();
        favoriteDto.setId(1);
    }

    /**
     * Test fetching all favorites.
     */
    @Test
    void getAllFavorites_ShouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(favorite));
        when(mapper.toDto(any())).thenReturn(favoriteDto);

        List<FavoritesDto> result = service.getAllFavorites();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(favoriteDto.getId(), result.getFirst().getId());
    }

    /**
     * Test fetching a favorite by ID when it exists.
     */
    @Test
    void getFavoriteById_ShouldReturnFavoriteDto_WhenExists() {
        when(repository.findById(1)).thenReturn(Optional.of(favorite));
        when(mapper.toDto(any())).thenReturn(favoriteDto);

        FavoritesDto result = service.getFavoriteById(1);

        assertNotNull(result);
        assertEquals(favoriteDto.getId(), result.getId());
    }

    /**
     * Test fetching a favorite by ID when it does not exist.
     */
    @Test
    void getFavoriteById_ShouldReturnNull_WhenNotExists() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        FavoritesDto result = service.getFavoriteById(1);

        assertNull(result);
    }

    /**
     * Test saving a favorite.
     */
    @Test
    void saveFavorite_ShouldReturnSavedFavorite() {
        when(mapper.toEntity(any())).thenReturn(favorite);
        when(repository.save(any())).thenReturn(favorite);
        when(mapper.toDto(any())).thenReturn(favoriteDto);

        FavoritesDto result = service.saveFavorite(favoriteDto);

        assertNotNull(result);
        assertEquals(favoriteDto.getId(), result.getId());
    }

    /**
     * Test deleting a favorite when it exists.
     */
    @Test
    void deleteFavorite_ShouldDelete_WhenExists() {
        when(repository.findById(1)).thenReturn(Optional.of(favorite));
        doNothing().when(repository).delete(any());

        assertDoesNotThrow(() -> service.deleteFavorite(1));
        verify(repository, times(1)).delete(favorite);
    }

    /**
     * Test deleting a favorite when it does not exist.
     */
    @Test
    void deleteFavorite_ShouldThrowException_WhenNotExists() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(OnlineGardenShopResourceNotFoundException.class, () -> service.deleteFavorite(1));

        assertEquals("Favorite not found with id 1", exception.getMessage());
    }
}
