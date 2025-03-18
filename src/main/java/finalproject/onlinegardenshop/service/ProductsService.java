package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.ProductCreateDto;
import finalproject.onlinegardenshop.dto.ProductsDto;
import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.entity.Categories;
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
import java.util.Optional;

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
                           FavoritesRepository favoritesRepository, CartItemsRepository cartItemsRepository, OrderItemsRepository orderItemsRepository, ProductsMapper mapper) {
        this.repository = repository;
        this.categoriesRepository = categoriesRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.favoritesRepository = favoritesRepository;
        this.mapper = mapper;
    }

    public List<ProductsDto> getAll() {
        List<Products> products = repository.findAll();
        logger.debug("Products retrieved from the database");
        logger.debug("Product ids: {}", () -> products.stream().map(Products::getId).toList());
        return mapper.entityListToDto(products);
    }

    public Optional<ProductsDto> getById(int id) {
        Optional<Products> product = repository.findById(id);
        ProductsDto productDto = mapper.entityToDto(product.orElse(null));
        return Optional.ofNullable(productDto);
    }

    public List<ProductsDto> getFilteredProducts(String category, Double minPrice, Double maxPrice, Boolean discount, Sort sort) {
        List<Products> products;

        // Если параметры пустые, возвращаем все товары с сортировкой по умолчанию
        if (category == null && minPrice == null && maxPrice == null && discount == null) {
            products = repository.findAll(sort != null ? sort : Sort.by("name"));
            return mapper.entityListToDto(products);
        }

        // Фильтрация с учетом скидки
        if (category != null && discount != null) {
            if (discount) {
                // Фильтрация товаров с скидкой (discountPrice > 0)
                if (minPrice != null && maxPrice != null) {
                    // Фильтрация по категории, цене и скидке
                    products = repository.findByCategory_NameAndPriceBetweenAndDiscountPriceGreaterThan(
                            category, minPrice, maxPrice, 0.0, sort);
                } else {
                    // Фильтрация по категории и скидке
                    products = repository.findByCategory_NameAndDiscountPriceGreaterThan(category, 0.0, sort);
                }
            } else {
                // Фильтрация товаров без скидки (discountPrice IS NULL)
                if (minPrice != null && maxPrice != null) {
                    // Фильтрация по категории, цене и отсутствию скидки
                    products = repository.findByCategory_NameAndPriceBetweenAndDiscountPriceIsNull(
                            category, minPrice, maxPrice, sort);
                } else {
                    // Фильтрация по категории и отсутствию скидки
                    products = repository.findByCategory_NameAndDiscountPriceIsNull(category, sort);
                }
            }
        }
        // Фильтрация по категории и цене
        else if (category != null && minPrice != null && maxPrice != null) {
            products = repository.findByCategory_NameAndPriceBetween(category, minPrice, maxPrice, sort);
        }
        // Фильтрация только по категории
        else if (category != null) {
            products = repository.findByCategory_Name(category, sort);
        }
        // Фильтрация только по цене
        else if (minPrice != null && maxPrice != null) {
            products = repository.findByPriceBetween(minPrice, maxPrice, sort);
        }
        // Фильтрация только по скидке
        else if (discount != null) {
            if (discount) {
                // Фильтрация товаров с скидкой (discountPrice > 0)
                products = repository.findByDiscountPriceGreaterThan(0.0, sort);
            } else {
                // Фильтрация товаров без скидки (discountPrice IS NULL)
                products = repository.findByDiscountPriceIsNull(sort);
            }
        }
        // Если ничего не передано, возвращаем все товары
        else {
            products = repository.findAll(sort);
        }
        // Преобразуем список сущностей в DTO и возвращаем
        return mapper.entityListToDto(products);
    }


    @Transactional
    public ProductsDto addProduct(ProductCreateDto dto) {

        Categories category = categoriesRepository.findByName(dto.getCategory())
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Category not found: " + dto.getCategory()));

        Products product = mapper.createDtoToEntity(dto);

        product.setCategory(category);

        Products savedProduct = repository.save(product);

        return mapper.entityToDto(savedProduct);
    }

    @Transactional
    public ProductsDto updateProduct(int id, ProductsDto dto) {
        Optional<Products> optional = repository.findById(id);
        if (optional.isPresent()) {
            Products product = optional.get();
            product.setName(dto.getName());
            product.setDescription(dto.getDescription());
            product.setPrice(dto.getPrice());
            product.setDiscountPrice(dto.getDiscountPrice()); // Теперь сохраняем null, если не передана скидка
            product.setImageUrl(dto.getImageUrl());

            if (dto.getCategory() != null) {
                Optional<Categories> categoryOptional = categoriesRepository.findByName(dto.getCategory());

                if (categoryOptional.isPresent()) {
                    Categories category = categoryOptional.get();
                    product.setCategory(category);
                    repository.save(product);
                } else {
                    throw new OnlineGardenShopResourceNotFoundException(
                            String.format("Category with name '%s' not found", dto.getCategory()));
                }
            }

            Products savedProduct = repository.save(product);
            return mapper.entityToDto(savedProduct);
        }

        throw new OnlineGardenShopResourceNotFoundException(String.format("Product with id %s not found", id));
    }

    @Transactional
    public void deleteProduct(int id) {
        // 1. Проверяем, существует ли продукт
        Products product = repository.findById(id)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Product not found"));

        // 2. Удаляем все записи в cart_items, связанные с этим продуктом
        cartItemsRepository.deleteByProductsId(id);

        // 3. Обнуляем product_id в order_items, но не удаляем заказы!
        orderItemsRepository.updateProductToNull(id);

        // ❗ НЕ УДАЛЯЕМ товар из favorites

        // 4. Удаляем сам продукт
        repository.deleteById(id);
    }

}

