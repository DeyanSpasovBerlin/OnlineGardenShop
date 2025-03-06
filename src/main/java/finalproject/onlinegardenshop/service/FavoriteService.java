package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.FavoriteDto;
import finalproject.onlinegardenshop.entity.Favorite;
import finalproject.onlinegardenshop.repository.FavoriteRepository;
import finalproject.onlinegardenshop.mapper.FavoriteMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository repository;
    private final FavoriteMapper mapper;

    public FavoriteService(FavoriteRepository repository, FavoriteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<FavoriteDto> getAllFavorites() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public FavoriteDto getFavoriteById(Integer id) {
        return repository.findById(id).map(mapper::toDto).orElse(null);
    }

    public FavoriteDto saveFavorite(FavoriteDto dto) {
        Favorite entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }
//    надо добавить метод удаление
}
