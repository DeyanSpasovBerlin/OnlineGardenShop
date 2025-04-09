package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.OrderCreateRequestDto;
import finalproject.onlinegardenshop.dto.OrderCreateRequestSaveOrderItemsDto;
import finalproject.onlinegardenshop.dto.OrdersDto;
import finalproject.onlinegardenshop.entity.OrderItems;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.entity.Users;
import finalproject.onlinegardenshop.service.OrdersMapperInjector;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;


@Mapper(componentModel = "spring",  uses = {OrdersMapperInjector.class})
public interface OrdersMapper {

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "users", source = "usersId", qualifiedByName = "usersFromId")
    Orders dtoToEntity(OrdersDto dto);

    @Mapping(target = "usersId", source = "users.id")
    @Mapping(target = "firstName", source = "users.firstName")
    @Mapping(target = "lastName", source = "users.lastName")
    @Mapping(target = "items", source = "orderItems")
    OrdersDto entityToDto(Orders entity);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.id", qualifiedByName = "productNameFromId")
    OrderCreateRequestSaveOrderItemsDto orderItemToDto(OrderItems orderItem);

    List<OrdersDto> entityListToDto(List<Orders> entities);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "contactPhone", ignore = true)
    @Mapping(target = "users", source = "usersId", qualifiedByName = "usersFromId")
    Orders dtoPostToEntity(OrderCreateRequestDto dto);

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "usersId", source = "users.id")
    OrderCreateRequestDto entityToDtopost(Orders entity);

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