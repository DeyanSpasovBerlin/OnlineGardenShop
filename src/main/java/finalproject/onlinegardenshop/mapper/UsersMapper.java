package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.UsersDto;
import finalproject.onlinegardenshop.entity.Users;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    Users dtoToEntity(UsersDto dto);

    UsersDto entityToDto(Users entity);

    List<UsersDto> entityListToDto(List<Users> entities);
}
