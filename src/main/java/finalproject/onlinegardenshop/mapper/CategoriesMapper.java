package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.CategoryCreateDto;
import finalproject.onlinegardenshop.dto.CategoriesDto;
import finalproject.onlinegardenshop.entity.Categories;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriesMapper {

    Categories dtoToEntity(CategoriesDto dto);

    CategoriesDto entityToDto(Categories entity);

    List<CategoriesDto> entityListToDto(List<Categories> categories);

    @Mapping(target = "id", ignore = true)
    Categories createDtoToEntity(CategoryCreateDto dto);

}

