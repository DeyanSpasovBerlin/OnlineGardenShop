package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.CreateOrderRequestDto;
import finalproject.onlinegardenshop.dto.CreateOrderRequestSaveOrderItemsDto;
import finalproject.onlinegardenshop.dto.OrdersDto;
import finalproject.onlinegardenshop.entity.OrderItems;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrdersMapper {

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "users", source = "usersId", qualifiedByName = "usersFromId")
    Orders dtoToEntity(OrdersDto dto);

    @Mapping(target = "usersId", source = "users.id")
    @Mapping(target = "items", source = "orderItems")
    OrdersDto entityToDto(Orders entity);

    @Mapping(target = "productId", source = "product.id") // FIXED!
    CreateOrderRequestSaveOrderItemsDto orderItemToDto(OrderItems orderItem);

    List<OrdersDto> entityListToDto(List<Orders> entities);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "contactPhone", ignore = true)
    @Mapping(target = "users", source = "usersId", qualifiedByName = "usersFromId")
    Orders dtoPostToEntity(CreateOrderRequestDto dto);

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "usersId", source = "users.id")
    CreateOrderRequestDto entityToDtopost(Orders entity);

//    @Mapping(target = "items", source = "orderItems")
//    OrdersDto entityToDto(Orders entity);

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
