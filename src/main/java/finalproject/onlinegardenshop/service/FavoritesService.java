package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.FavoritesDto;
import finalproject.onlinegardenshop.entity.Favorites;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.repository.FavoritesRepository;
import finalproject.onlinegardenshop.mapper.FavoritesMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritesService {
    private final FavoritesRepository repository;
    private final FavoritesMapper mapper;

    public FavoritesService(FavoritesRepository repository, FavoritesMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<FavoritesDto> getAllFavorites() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public FavoritesDto getFavoriteById(Integer id) {
        return repository.findById(id).map(mapper::toDto).orElse(null);
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
