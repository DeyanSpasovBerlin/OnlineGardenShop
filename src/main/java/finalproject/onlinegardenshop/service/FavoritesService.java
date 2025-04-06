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

    public List<FavoritesDto> getAllFavorites() {
        Users currentUser = getAuthenticatedUser();
        return repository.findAllByUser(currentUser).stream()
                .map(mapper::toDto) // Используем method reference
                .collect(Collectors.toList());
    }

    public FavoritesDto getFavoriteById(Integer id) {
        Favorites favorite = repository.findById(id)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Favorite not found with id " + id));

        checkAuthorization(favorite.getUser());

        return mapper.toDto(favorite);
    }

    public FavoritesDto saveFavorite(FavoritesDto dto) {
        Favorites entity = mapper.toEntity(dto);
        entity.setUser(getAuthenticatedUser());
        return mapper.toDto(repository.save(entity));
    }

    public void deleteFavorite(Integer id) {
        Favorites favorite = repository.findById(id)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Favorite not found with id " + id));

        checkAuthorization(favorite.getUser());

        repository.delete(favorite);
    }

    private Users getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("User not found with email: " + email));
    }

    private void checkAuthorization(Users owner) {
        Users currentUser = getAuthenticatedUser();
        if (!owner.equals(currentUser)) {
            throw new SecurityException("You are not authorized to access this resource");
        }
    }
}
