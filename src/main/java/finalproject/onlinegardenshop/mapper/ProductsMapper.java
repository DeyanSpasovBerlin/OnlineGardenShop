package finalproject.onlinegardenshop.mapper;

import finalproject.onlinegardenshop.dto.ProductCreateDto;
import finalproject.onlinegardenshop.dto.ProductsDto;
import finalproject.onlinegardenshop.entity.Categories;
import finalproject.onlinegardenshop.entity.Products;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = CategoriesMapper.class)
public interface ProductsMapper {

    @Mapping(target = "category", source = "category", qualifiedByName = "categoryNameToEntity")
    Products createDtoToEntity(ProductCreateDto dto);

    @Mapping(target = "category", source = "category", qualifiedByName = "categoryEntityToName")
    @Mapping(target = "createdAtInstant", source = "createdAt")
    @Mapping(target = "updatedAtInstant", source = "updatedAt")
    ProductsDto entityToDto(Products entity);

    List<ProductsDto> entityListToDto(List<Products> productsList);

    @Named("categoryNameToEntity")
    default Categories categoryNameToEntity(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            return null;
        }
        Categories category = new Categories();
        category.setName(categoryName);
        return category;
    }

    @Named("categoryEntityToName")
    default String categoryEntityToName(Categories category) {
        return (category != null) ? category.getName() : null;
    }

}

