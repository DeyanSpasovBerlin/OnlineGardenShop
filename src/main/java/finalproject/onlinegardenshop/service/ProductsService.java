package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.ProductCreateDto;
import finalproject.onlinegardenshop.dto.ProductsDto;
import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.entity.Categories;
import finalproject.onlinegardenshop.exception.OnlineGardenShopBadRequestException;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.ProductsMapper;
import finalproject.onlinegardenshop.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductsService {

    private final ProductsRepository repository;
    private final CategoriesRepository categoriesRepository;
    private final FavoritesRepository favoritesRepository;
    private final CartItemsRepository cartItemsRepository;
    private final OrderItemsRepository orderItemsRepository;
    private static final Logger logger = LogManager.getLogger(ProductsService.class);
    private final ProductsMapper mapper;

    @Autowired
    public ProductsService(ProductsRepository repository, CategoriesRepository categoriesRepository,
                           FavoritesRepository favoritesRepository, CartItemsRepository cartItemsRepository,
                           OrderItemsRepository orderItemsRepository, ProductsMapper mapper)
    {
        this.repository = repository;
        this.categoriesRepository = categoriesRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.favoritesRepository = favoritesRepository;
        this.mapper = mapper;
    }

    public List<ProductsDto> getAll() {
        List<Products> products = repository.findAll();
        if (products.isEmpty()) {
            throw new OnlineGardenShopResourceNotFoundException("No products found");
        }
            logger.debug("Products retrieved from the database");
            logger.debug("Product ids: {}", () -> products.stream().map(Products::getId).toList());
            return mapper.entityListToDto(products);
    }

    public ProductsDto getById(int id) {
    return repository.findById(id)
            .map(mapper::entityToDto)
            .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Product with id " + id + " not found"));
    }

    public List<ProductsDto> getFilteredProducts(String category, Double minPrice, Double maxPrice, Boolean discount, Sort sort) {
        List<Products> products;

        if (category != null && minPrice != null && maxPrice != null && discount != null) {
            products = discount
                    ? repository.findByCategory_NameAndPriceBetweenAndDiscountPriceGreaterThan(category, minPrice, maxPrice, 0.0, sort)
                    : repository.findByCategory_NameAndPriceBetweenAndDiscountPriceIsNull(category, minPrice, maxPrice, sort);
        } else if (category != null && discount != null) {
            products = discount
                    ? repository.findByCategory_NameAndDiscountPriceGreaterThan(category, 0.0, sort)
                    : repository.findByCategory_NameAndDiscountPriceIsNull(category, sort);
        } else if (category != null && minPrice != null && maxPrice != null) {
            products = repository.findByCategory_NameAndPriceBetween(category, minPrice, maxPrice, sort);
        } else if (category != null) {
            products = repository.findByCategory_Name(category, sort);
        } else if (minPrice != null && maxPrice != null) {
            products = repository.findByPriceBetween(minPrice, maxPrice, sort);
        } else if (discount != null) {
            products = discount
                    ? repository.findByDiscountPriceGreaterThan(0.0, sort)
                    : repository.findByDiscountPriceIsNull(sort);
        } else {
            products = repository.findAll(sort);
        }

        if (products.isEmpty()) {
            throw new OnlineGardenShopResourceNotFoundException("No products with the given parameters found");
        }

        return mapper.entityListToDto(products);
    }

    @Transactional
    public ProductsDto addProduct(ProductCreateDto dto) {

        Categories category = categoriesRepository.findByName(dto.getCategory())
                .orElseThrow(() -> new OnlineGardenShopBadRequestException("Category " + dto.getCategory() + " not found"));

        Products product = mapper.createDtoToEntity(dto);

        product.setCategory(category);

        Products savedProduct = repository.save(product);

        return mapper.entityToDto(savedProduct);
    }

    @Transactional
    public ProductsDto updateProduct(int id, ProductsDto dto) {
        Products product = repository.findById(id)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Product with id " + id + " not found"));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDiscountPrice(dto.getDiscountPrice());
        product.setImageUrl(dto.getImageUrl());

        if (dto.getCategory() != null) {
            Categories category = categoriesRepository.findByName(dto.getCategory())
                    .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Category " + dto.getCategory() + " not found"));
            product.setCategory(category);
        }

        Products savedProduct = repository.save(product);
        return mapper.entityToDto(savedProduct);
    }

    @Transactional
    public void deleteProduct(int id) {
        Products product = repository.findById(id)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Product with id " + id + " not found"));

        cartItemsRepository.deleteByProductsId(id);
        orderItemsRepository.updateProductToNull(id);
        repository.deleteById(id);
    }

}

