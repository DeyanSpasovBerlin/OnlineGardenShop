package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.CartItemsDto;
import finalproject.onlinegardenshop.entity.Cart;
import finalproject.onlinegardenshop.entity.CartItems;
import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.service.OrdersMapperInjector;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = OrdersMapperInjector.class)
public interface CartItemsMapper {

    @Mapping(target = "cart", source = "cartId", qualifiedByName = "cartFromId")
    @Mapping(target = "products", source = "productsId", qualifiedByName = "productsFromId")
    CartItems dtoToEntity(CartItemsDto cartDto);

    @Mapping(target = "cartId", source = "cart.id")
    @Mapping(target = "productsId", source = "products.id")
    @Mapping(target = "productsName", source = "products.id", qualifiedByName = "productNameFromId")
    @Mapping(target = "productsPrice", source = "products.price")
    CartItemsDto entityToDto(CartItems entity);

    List<CartItemsDto> entityListToDto(List<CartItems> entities);

    @Named("cartFromId")
    default Cart cartFromId(Integer id) {
        if (id == null) {
            return null;
        }
        Cart cart = new Cart();
        cart.setId(id);
        return cart;
    }

    @Named("productsFromId")
    default Products productsFromId(Integer id) {
        if (id == null) {
            return null;
        }
        Products product = new Products();
        product.setId(id);
        return product;
    }

}
