package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.CartDto;
import finalproject.onlinegardenshop.dto.CartFullDto;
import finalproject.onlinegardenshop.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = CartItemsMapper.class)
public interface CartMapper {

    @Mapping(target = "users", ignore = true)//"user" . old
    Cart dtoToEntity(CartDto cartDto);

    @Mapping(target = "usersId", source = "users.id")//target = "userId", source = "user.id" . old
    CartDto entityToDto(Cart entity);

    List<CartDto> entityListToDto(List<Cart> entities);

    List<CartFullDto> entityFullListToDto(List<Cart> entities);

    @Mapping(target = "users", ignore = true)//"user" . old
    Cart dtoFullToEntity(CartFullDto cartDto);

    @Mapping(target = "usersId", source = "users.id")
    CartFullDto entityToFullDto(Cart entity);
}
