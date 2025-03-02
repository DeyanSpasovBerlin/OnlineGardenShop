package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.FavoriteDto;
import finalproject.onlinegardenshop.entity.Favorite;

public class FavoriteMapper {
    public static FavoriteDto toDto(Favorite favorite) {
        return new FavoriteDto(
                favorite.getId(),
                favorite.getUser().getId(),
                favorite.getProduct().getId()
        );
    }

    public static Favorite toEntity(FavoriteDto dto) {
        Favorite favorite = new Favorite();
        favorite.setId(dto.getId());
        return favorite;
    }
}
