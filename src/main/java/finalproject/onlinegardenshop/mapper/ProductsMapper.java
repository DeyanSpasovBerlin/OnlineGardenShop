package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.ProductCreateDto;
import finalproject.onlinegardenshop.dto.ProductsDto;
import finalproject.onlinegardenshop.entity.Products;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductsMapper {

    Products dtoToEntity(ProductsDto dto);
    ProductsDto entityToDto(Products entity);
    List<ProductsDto> entityListToDto(List<Products> productsList);

    @Mapping(target = "id", ignore = true)
    Products createDtoToEntity(ProductCreateDto dto);

}
