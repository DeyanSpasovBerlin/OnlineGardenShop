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

    @Mapping(target = "users", ignore = true)//"user" . old
    Cart dtoToEntity(CartDto cartDto);

    @Mapping(target = "usersId", source = "users.id")//target = "userId", source = "user.id" . old
    CartDto entityToDto(Cart entity);

    List<CartDto> entityListToDto(List<Cart> entities);

    List<CartFullDto> entityFullListToDto(List<Cart> entities);

    @Mapping(target = "users", source = "usersId", qualifiedByName = "usersFromId")
    Cart dtoFullToEntity(CartFullDto cartDto);

    @Mapping(target = "usersId", source = "users.id")
    @Mapping(target = "firstName", source = "users.firstName")  // show firstName in Orders
    @Mapping(target = "lastName", source = "users.lastName")    // show lastName in Orders
    @Mapping(target = "cartItems", source = "cartItems")
    CartFullDto entityToFullDto(Cart entity);

    //to show in addToCart product name and price
    @Mapping(target = "productsName", source = "products.name") // Map product name
    @Mapping(target = "productsPrice", source = "products.price") // Map product price
    CartItemsDto entityToDto(CartItems cartsItem);  // Add this method to map product info

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
