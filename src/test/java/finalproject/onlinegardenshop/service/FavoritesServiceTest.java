package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.FavoritesDto;
import finalproject.onlinegardenshop.entity.Favorites;
import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.entity.Users;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.FavoritesMapper;
import finalproject.onlinegardenshop.repository.FavoritesRepository;
import finalproject.onlinegardenshop.repository.ProductsRepository;
import finalproject.onlinegardenshop.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoritesServiceTest {

    @Mock
    private FavoritesRepository favoritesRepository;
    @Mock
    private FavoritesMapper favoritesMapper;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private ProductsRepository productsRepository;

    @InjectMocks
    private FavoritesService favoritesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("test@example.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getAllFavorites_shouldReturnMappedFavoritesList() {
        Users user = new Users();
        List<Favorites> favoritesList = List.of(new Favorites());
        List<FavoritesDto> dtoList = List.of(new FavoritesDto());

        when(usersRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(favoritesRepository.findAllByUser(user)).thenReturn(favoritesList);
        when(favoritesMapper.toDto(any(Favorites.class))).thenReturn(dtoList.get(0));

        List<FavoritesDto> result = favoritesService.getAllFavorites();

        assertEquals(1, result.size());
        verify(favoritesRepository).findAllByUser(user);
    }

    @Test
    void saveFavorite_shouldSaveAndReturnDto() {
        Users user = new Users();
        Products product = new Products();
        FavoritesDto dto = new FavoritesDto();
        dto.setProductsId(42);
        Favorites entity = new Favorites();

        when(usersRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(favoritesMapper.toEntity(dto)).thenReturn(entity);
        when(productsRepository.findById(42)).thenReturn(Optional.of(product));
        when(favoritesRepository.save(entity)).thenReturn(entity);
        when(favoritesMapper.toDto(entity)).thenReturn(dto);

        FavoritesDto result = favoritesService.saveFavorite(dto);

        assertEquals(dto, result);
        verify(favoritesRepository).save(entity);
    }

    @Test
    void deleteFavorite_shouldDeleteIfOwnedByUser() {
        Users user = new Users();
        Favorites favorite = new Favorites();
        favorite.setUser(user);

        when(usersRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(favoritesRepository.findById(1)).thenReturn(Optional.of(favorite));

        favoritesService.deleteFavorite(1);

        verify(favoritesRepository).delete(favorite);
    }

    @Test
    void deleteFavorite_shouldThrowIfFavoriteNotFound() {
        when(usersRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new Users()));
        when(favoritesRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(OnlineGardenShopResourceNotFoundException.class,
                () -> favoritesService.deleteFavorite(99));
    }
}
