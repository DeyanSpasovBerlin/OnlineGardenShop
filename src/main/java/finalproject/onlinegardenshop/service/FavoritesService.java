package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.FavoritesDto;
import finalproject.onlinegardenshop.entity.Favorites;
import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.repository.FavoritesRepository;
import finalproject.onlinegardenshop.mapper.FavoritesMapper;
import finalproject.onlinegardenshop.repository.ProductsRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritesService {
    private final FavoritesRepository repository;
    private final ProductsRepository productsRepository; //Добавлено в целях метода удаления продукта в ProductsService
    private final FavoritesMapper mapper;

    public FavoritesService(FavoritesRepository repository, ProductsRepository productsRepository, FavoritesMapper mapper) {
        this.repository = repository;
        this.productsRepository = productsRepository;
        this.mapper = mapper;
    }

    public List<FavoritesDto> getAllFavorites() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public FavoritesDto getFavoriteById(Integer id) {
        return repository.findById(id).map(mapper::toDto).orElse(null);
    }

    //Добавлено в целях удаления продукта в ProductsService
    public List<FavoritesDto> getFavoritesByUserId(Integer userId) {
        List<Favorites> favorites = repository.findByUserId(userId);

        return favorites.stream().map(favorite -> {
            Products product = favorite.getProduct();
            boolean isAvailable = productsRepository.existsById(product.getId()); // Проверяем, есть ли товар

            return new FavoritesDto(
                    favorite.getId(),
                    product.getId(),
                    isAvailable ? "available" : "not available" // ✅ Добавляем статус
            );
        }).toList();
    }


    public FavoritesDto saveFavorite(FavoritesDto dto) {
        Favorites entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public void deleteFavorite(Integer id) {
        Favorites favorite = repository.findById(id)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Favorite not found with id " + id));
        repository.delete(favorite);
    }
}
