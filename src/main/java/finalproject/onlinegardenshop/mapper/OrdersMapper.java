package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.CreateOrderRequestDto;
import finalproject.onlinegardenshop.dto.CreateOrderRequestSaveOrderItemsDto;
import finalproject.onlinegardenshop.dto.OrdersDto;
import finalproject.onlinegardenshop.dto.RevenueReportDto;
import finalproject.onlinegardenshop.entity.OrderItems;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.entity.Users;
import finalproject.onlinegardenshop.service.OrdersMapperInjector;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring",  uses = {OrdersMapperInjector.class})//inject ProductHelper
public interface OrdersMapper {

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "users", source = "usersId", qualifiedByName = "usersFromId")
    Orders dtoToEntity(OrdersDto dto);

    @Mapping(target = "usersId", source = "users.id")//this show userId=null
    // and deletedUserId=-userId in Orders for deleted user
    @Mapping(target = "firstName", source = "users.firstName")  // show firstName in Orders
    @Mapping(target = "lastName", source = "users.lastName")    // show lastName in Orders
    @Mapping(target = "items", source = "orderItems")
    OrdersDto entityToDto(Orders entity);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.id", qualifiedByName = "productNameFromId")//ето инжекция product name from ProductHelper
//    @Mapping(target = "firstName", source = "order.users.id", qualifiedByName = "userFirstName")
//    @Mapping(target = "lastName", source = "order.users.id", qualifiedByName = "userLasttName")
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

    @Named("usersFromId")
    default Users usersFromId(Integer id){
        if(id ==0){
            return null;
        }
        Users users = new Users();
        users.setId(id);
        return users;
    }

//    // Преобразуваме един Object[] в RevenueReportDto -> getRevenueForLast10Days()->RevenueReportDto
//    @Mapping(target = "period", source = "0")  // Първият елемент от Object[]
//    @Mapping(target = "totalRevenue", source = "1")  // Вторият елемент от Object[]
//    RevenueReportDto objectArrayToRevenueReportDto(Object[] data);
//    // Преобразуваме списък от Object[] в списък от RevenueReportDto
//    default List<RevenueReportDto> mapToRevenueReportDtoList(List<Object[]> data) {
//        return data.stream()
//                .map(this::objectArrayToRevenueReportDto)
//                .collect(Collectors.toList());
//    }

}