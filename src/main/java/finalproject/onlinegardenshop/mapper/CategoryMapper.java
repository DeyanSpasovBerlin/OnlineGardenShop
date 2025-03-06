package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.CategoryCreateDto;
import finalproject.onlinegardenshop.dto.CategoryDto;
import finalproject.onlinegardenshop.entity.Categories;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Categories dtoToEntity(CategoryDto dto);

    CategoryDto entityToDto(Categories entity);

    List<CategoryDto> entityListToDto(List<Categories> categories);

    @Mapping(target = "id", ignore = true)
    Categories createDtoToEntity(CategoryCreateDto dto);

}

