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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritesService {
    private final FavoritesRepository repository;
    private final FavoritesMapper mapper;
    private final UsersRepository userRepository;
    private final ProductsRepository productsRepository;

    public FavoritesService(
            FavoritesRepository repository,
            @Qualifier("favoritesMapperImpl") FavoritesMapper mapper,
            UsersRepository userRepository, ProductsRepository productsRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.productsRepository = productsRepository;
    }

    public List<FavoritesDto> getAllFavorites() {
        Users currentUser = getAuthenticatedUser();
        return repository.findAllByUser(currentUser).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

@Transactional
public FavoritesDto saveFavorite(FavoritesDto dto) {
    Users currentUser = getAuthenticatedUser();
    Products product = productsRepository.findById(dto.getProductsId())
            .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Product not found with id " + dto.getProductsId()));

    if (repository.findByUserAndProduct(currentUser, product).isPresent()) {
        throw new RuntimeException("This product is already in your favorites");
    }

    Favorites entity = mapper.toEntity(dto);
    entity.setUser(currentUser);
    entity.setProduct(product);
    return mapper.toDto(repository.save(entity));
}

    public void deleteFavorite(Integer id) {
        Users currentUser = getAuthenticatedUser();
        Favorites favorite = repository.findById(id)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Favorite not found with id " + id));
        if(favorite.getUser().equals(currentUser)) {
            repository.delete(favorite);
        }
    }

    private Users getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("User not found with email: " + email));
    }
}