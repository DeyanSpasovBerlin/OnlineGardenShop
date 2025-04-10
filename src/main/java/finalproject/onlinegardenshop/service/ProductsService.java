package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.ProductCreateDto;
import finalproject.onlinegardenshop.dto.ProductsDto;
import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.entity.Categories;
import finalproject.onlinegardenshop.exception.OnlineGardenShopBadRequestException;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.ProductsMapper;
import finalproject.onlinegardenshop.repository.*;
import finalproject.onlinegardenshop.specification.ProductsSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    public Page<ProductsDto> getFilteredProductsDynamic(Map<String, String> filters) {
        String sortParam = filters.getOrDefault("sort", "name");
        Sort sort = getSort(sortParam);

        int page = Integer.parseInt(filters.getOrDefault("page", "0"));
        int size = Integer.parseInt(filters.getOrDefault("size", "10"));
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Products> spec = Specification.where(null);

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (List.of("sort", "page", "size").contains(key)) continue;

            Specification<Products> currentSpec = ProductsSpecification.fromParam(key, value);
            if (currentSpec != null) {
                spec = spec.and(currentSpec);
            }
        }

        Page<Products> pageResult = repository.findAll(spec, pageable);

        if (pageResult.isEmpty()) {
            throw new OnlineGardenShopResourceNotFoundException("No products with the given parameters found");
        }

        return pageResult.map(mapper::entityToDto);
    }

    public Sort getSort(String sortParam) {
        return switch (sortParam) {
            case "priceAsc" -> Sort.by(Sort.Order.asc("price"));
            case "priceDesc" -> Sort.by(Sort.Order.desc("price"));
            case "dateAsc" -> Sort.by(Sort.Order.asc("createdAt"));
            case "dateDesc" -> Sort.by(Sort.Order.desc("createdAt"));
            case "nameDesc" -> Sort.by(Sort.Order.desc("name"));
            default -> Sort.by(Sort.Order.asc("name"));
        };
    }

    public ProductsDto getDealOfTheDay() {
        return repository.findBestDiscountedProduct()
                .map(mapper::entityToDto)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("No discounted products available"));
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

