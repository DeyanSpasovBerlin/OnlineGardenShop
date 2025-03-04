package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.ProductCreateDto;
import finalproject.onlinegardenshop.dto.ProductDto;
import finalproject.onlinegardenshop.entity.Products;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Products dtoToEntity(ProductDto dto);
    ProductDto entityToDto(Products entity);
    List<ProductDto> entityListToDto(List<Products> productsList);

    @Mapping(target = "id", ignore = true)
    Products createDtoToEntity(ProductCreateDto dto);

}
