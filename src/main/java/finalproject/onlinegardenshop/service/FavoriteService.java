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

    public FavoriteService(FavoriteRepository repository) {
        this.repository = repository;
    }

    public List<FavoriteDto> getAllFavorites() {
        return repository.findAll().stream().map(FavoriteMapper::toDto).collect(Collectors.toList());
    }

    public FavoriteDto getFavoriteById(Integer id) {
        return repository.findById(id).map(FavoriteMapper::toDto).orElse(null);
    }

    public FavoriteDto saveFavorite(FavoriteDto dto) {
        Favorite entity = FavoriteMapper.toEntity(dto);
        return FavoriteMapper.toDto(repository.save(entity));
    }
}
