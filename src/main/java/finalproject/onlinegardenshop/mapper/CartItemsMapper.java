//package finalproject.onlinegardenshop.mapper;
//
//import finalproject.onlinegardenshop.dto.CartItemstDto;
//import finalproject.onlinegardenshop.entity.CartItems;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//import java.util.List;
//
//@Mapper(componentModel = "spring")
//public interface CartItemsMapper {
//
//    @Mapping(target = "cart", ignore = true)
//    @Mapping(target = "products", ignore = true)
//    CartItems dtoToEntity(CartItemstDto cartDto);
//
//    @Mapping(target = "cartId", source = "cart.id")
//    @Mapping(target = "productsId", source = "products.id")
//    CartItemstDto entityToDto(CartItems entity);
//
//    List<CartItemstDto> entityListToDto(List<CartItems> entities);
//
//
//}
