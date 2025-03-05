package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.OrdersDto;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrdersMapper {

    @Mapping(target = "users", source = "usersId", qualifiedByName = "usersFromId")
    Orders dtoToEntity(OrdersDto dto);

    @Mapping(target = "usersId", source = "users.id")
    OrdersDto entityToDto(Orders entity);

    List<OrdersDto> entityListToDto(List<Orders> entities);

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
