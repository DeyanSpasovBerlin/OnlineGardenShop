package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.FavoritesDto;
import finalproject.onlinegardenshop.entity.Favorites;
import finalproject.onlinegardenshop.entity.Users;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.FavoritesMapper;
import finalproject.onlinegardenshop.repository.FavoritesRepository;
import finalproject.onlinegardenshop.repository.UsersRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritesService {
    private final FavoritesRepository repository;
    private final FavoritesMapper mapper;
    private final UsersRepository userRepository;

    public FavoritesService(
            FavoritesRepository repository,
            @Qualifier("favoritesMapperImpl") FavoritesMapper mapper,
            UsersRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    // Получение всех избранных товаров текущего пользователя
    public List<FavoritesDto> getAllFavorites() {
        Users currentUser = getAuthenticatedUser();
        return repository.findAllByUser(currentUser).stream()
                .map(mapper::toDto) // Используем method reference для преобразования в DTO
                .collect(Collectors.toList());
    }

    // Сохранение избранного товара для текущего пользователя
    public FavoritesDto saveFavorite(FavoritesDto dto) {
        Favorites entity = mapper.toEntity(dto);

        // Получаем текущего аутентифицированного пользователя
        Users currentUser = getAuthenticatedUser();
        entity.setUser(currentUser); // Устанавливаем текущего пользователя для избранного товара

        return mapper.toDto(repository.save(entity));
    }

    // Удаление избранного товара
    public void deleteFavorite(Integer id) {
        Users currentUser = getAuthenticatedUser();
        Favorites favorite = repository.findById(id)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Favorite not found with id " + id));
        if(favorite.getUser().equals(currentUser)) {
            repository.delete(favorite);
        }
    }

    // Получение аутентифицированного пользователя из контекста
    private Users getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("User not found with email: " + email));
    }
}
