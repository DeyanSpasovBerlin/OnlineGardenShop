package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.CartDto;
import finalproject.onlinegardenshop.dto.UsersDto;
import finalproject.onlinegardenshop.entity.Cart;
import finalproject.onlinegardenshop.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "user", ignore = true)
    Cart dtoToEntity(CartDto cartDto);

    @Mapping(target = "userId", source = "user.id")
    CartDto entityToDto(Cart entity);

    List<CartDto> entityListToDto(List<Cart> entities);


}
