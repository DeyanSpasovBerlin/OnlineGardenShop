package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.CartDto;
import finalproject.onlinegardenshop.dto.CartFullDto;
import finalproject.onlinegardenshop.dto.CartItemsDto;
import finalproject.onlinegardenshop.entity.Cart;
import finalproject.onlinegardenshop.entity.CartItems;
import finalproject.onlinegardenshop.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = CartItemsMapper.class)
public interface CartMapper {

    @Mapping(target = "users", ignore = true)
    Cart dtoToEntity(CartDto cartDto);

    @Mapping(target = "usersId", source = "users.id")
    CartDto entityToDto(Cart entity);

    List<CartDto> entityListToDto(List<Cart> entities);

    List<CartFullDto> entityFullListToDto(List<Cart> entities);

    @Mapping(target = "users", source = "usersId", qualifiedByName = "usersFromId")
    Cart dtoFullToEntity(CartFullDto cartDto);

    @Mapping(target = "usersId", source = "users.id")
    @Mapping(target = "firstName", source = "users.firstName")
    @Mapping(target = "lastName", source = "users.lastName")
    @Mapping(target = "cartItems", source = "cartItems")
    CartFullDto entityToFullDto(Cart entity);

    @Mapping(target = "productsName", source = "products.name")
    @Mapping(target = "productsPrice", source = "products.price")
    CartItemsDto entityToDto(CartItems cartsItem);

    @Named("usersFromId")
    default Users usersFromId(Integer id){
        if(id ==0){
            return null;
        }
        Users users = new Users();
        users.setId(id);
        return users;
    }

}
