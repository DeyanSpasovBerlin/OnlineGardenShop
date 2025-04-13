package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.FavoritesDto;
import finalproject.onlinegardenshop.entity.Favorites;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoritesMapper {
    @Mapping(source = "product.id", target = "productsId")
    @Mapping(source = "user.id", target = "userId")
    FavoritesDto toDto(Favorites favorite);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    Favorites toEntity(FavoritesDto dto);
}

