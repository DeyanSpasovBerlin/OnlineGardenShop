package finalproject.onlinegardenshop.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import finalproject.onlinegardenshop.dto.ProductCreateDto;
import finalproject.onlinegardenshop.dto.ProductsDto;
import finalproject.onlinegardenshop.entity.Categories;
import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.ProductsMapper;
import finalproject.onlinegardenshop.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductsServiceTest {

    @Mock
    private ProductsRepository productsRepository;
    @Mock
    private CategoriesRepository categoriesRepository;
    @Mock
    private FavoritesRepository favoritesRepository;
    @Mock
    private CartItemsRepository cartItemsRepository;
    @Mock
    private OrderItemsRepository orderItemsRepository;
    @Mock
    private ProductsMapper productsMapper;

    private ProductsService productsService;

    @BeforeEach
    void setUp() {
        productsService = new ProductsService(productsRepository, categoriesRepository, favoritesRepository,
                                              cartItemsRepository, orderItemsRepository, productsMapper);
    }

    @Test
    void testGetAllProducts_success() {
        List<Products> productsList = List.of(new Products(), new Products());
        List<ProductsDto> dtoList = List.of(new ProductsDto(), new ProductsDto());

        when(productsRepository.findAll()).thenReturn(productsList);
        when(productsMapper.entityListToDto(productsList)).thenReturn(dtoList);

        List<ProductsDto> result = productsService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllProducts_noProductsFound() {
        when(productsRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(OnlineGardenShopResourceNotFoundException.class, () -> productsService.getAll());
    }

    @Test
    void testGetById_success() {
        Products product = new Products();
        product.setId(1);
        ProductsDto dto = new ProductsDto();
        dto.setId(1);

        when(productsRepository.findById(1)).thenReturn(Optional.of(product));
        when(productsMapper.entityToDto(product)).thenReturn(dto);

        ProductsDto result = productsService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testGetById_productNotFound() {
        when(productsRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(OnlineGardenShopResourceNotFoundException.class, () -> productsService.getById(1));
    }

    @Test
    void testGetFilteredProductsDynamic_success() {
        Map<String, String> filters = new HashMap<>();
        filters.put("page", "0");
        filters.put("size", "10");

        Page<Products> productPage = new PageImpl<>(List.of(new Products()));
        Page<ProductsDto> dtoPage = new PageImpl<>(List.of(new ProductsDto()));

        when(productsRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(productPage);
        when(productsMapper.entityToDto(any(Products.class))).thenReturn(new ProductsDto());

        Page<ProductsDto> result = productsService.getFilteredProductsDynamic(filters);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetFilteredProductsDynamic_noResults() {
        Map<String, String> filters = new HashMap<>();
        filters.put("page", "0");
        filters.put("size", "10");
        filters.put("name", "Rose");

        Page<Products> emptyPage = Page.empty();
        when(productsRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(emptyPage);

        assertThrows(OnlineGardenShopResourceNotFoundException.class, () -> productsService.getFilteredProductsDynamic(filters));
    }

    @Test
    void testGetDealOfTheDay_success() {
        Products bestProduct = new Products();
        ProductsDto dto = new ProductsDto();

        when(productsRepository.findBestDiscountedProduct()).thenReturn(Optional.of(bestProduct));
        when(productsMapper.entityToDto(bestProduct)).thenReturn(dto);

        ProductsDto result = productsService.getDealOfTheDay();

        assertNotNull(result);
    }

    @Test
    void testGetDealOfTheDay_notFound() {
        when(productsRepository.findBestDiscountedProduct()).thenReturn(Optional.empty());

        assertThrows(OnlineGardenShopResourceNotFoundException.class, () -> productsService.getDealOfTheDay());
    }

    @Test
    void testGetSort_priceAsc() {
        Sort sort = productsService.getSort("priceAsc");
        assertEquals(Sort.by(Sort.Order.asc("price")), sort);
    }

    @Test
    void testGetSort_priceDesc() {
        Sort sort = productsService.getSort("priceDesc");
        assertEquals(Sort.by(Sort.Order.desc("price")), sort);
    }

    @Test
    void testGetSort_dateAsc() {
        Sort sort = productsService.getSort("dateAsc");
        assertEquals(Sort.by(Sort.Order.asc("createdAt")), sort);
    }

    @Test
    void testGetSort_dateDesc() {
        Sort sort = productsService.getSort("dateDesc");
        assertEquals(Sort.by(Sort.Order.desc("createdAt")), sort);
    }

    @Test
    void testGetSort_nameDesc() {
        Sort sort = productsService.getSort("nameDesc");
        assertEquals(Sort.by(Sort.Order.desc("name")), sort);
    }

    @Test
    void testGetSort_default() {
        Sort sort = productsService.getSort("unknown");
        assertEquals(Sort.by(Sort.Order.asc("name")), sort);
    }

    @Test
    void testAddProduct_success() {
        ProductCreateDto dto = new ProductCreateDto();
        dto.setCategory("Flowers");

        Categories category = new Categories();
        Products product = new Products();
        Products savedProduct = new Products();
        ProductsDto expectedDto = new ProductsDto();

        when(categoriesRepository.findByName("Flowers")).thenReturn(Optional.of(category));
        when(productsMapper.createDtoToEntity(dto)).thenReturn(product);
        when(productsRepository.save(product)).thenReturn(savedProduct);
        when(productsMapper.entityToDto(savedProduct)).thenReturn(expectedDto);

        ProductsDto result = productsService.addProduct(dto);

        assertNotNull(result);
    }

    @Test
    void testUpdateProduct_success() {
        int productId = 1;
        ProductsDto dto = new ProductsDto();
        dto.setName("Updated");
        dto.setCategory("Trees");

        Products existing = new Products();
        Categories category = new Categories();
        ProductsDto expectedDto = new ProductsDto();

        when(productsRepository.findById(productId)).thenReturn(Optional.of(existing));
        when(categoriesRepository.findByName("Trees")).thenReturn(Optional.of(category));
        when(productsRepository.save(existing)).thenReturn(existing);
        when(productsMapper.entityToDto(existing)).thenReturn(expectedDto);

        ProductsDto result = productsService.updateProduct(productId, dto);

        assertNotNull(result);
    }

    @Test
    void testDeleteProduct_success() {
        int productId = 1;
        Products product = new Products();

        when(productsRepository.findById(productId)).thenReturn(Optional.of(product));

        productsService.deleteProduct(productId);

        verify(cartItemsRepository).deleteByProductsId(productId);
        verify(orderItemsRepository).updateProductToNull(productId);
        verify(productsRepository).deleteById(productId);
    }

    @Test
    void testDeleteProduct_productNotFound() {
        when(productsRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(OnlineGardenShopResourceNotFoundException.class, () -> productsService.deleteProduct(1));
    }


}

