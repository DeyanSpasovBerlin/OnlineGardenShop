package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.FavoriteDto;
import finalproject.onlinegardenshop.entity.Favorite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {
    @Mapping(source = "product.id", target = "productsId")
    FavoriteDto toDto(Favorite favorite);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    Favorite toEntity(FavoriteDto dto);
}

