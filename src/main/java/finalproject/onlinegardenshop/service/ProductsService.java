package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.ProductCreateDto;
import finalproject.onlinegardenshop.dto.ProductsDto;
import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.ProductsMapper;
import finalproject.onlinegardenshop.repository.ProductsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductsService {

    private final ProductsRepository repository;
    private static final Logger logger = LogManager.getLogger(ProductsService.class);
    private final ProductsMapper mapper;

    @Autowired
    public ProductsService(ProductsRepository repository, ProductsMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ProductsDto> getAll(){
        List<Products> products = repository.findAll();
        logger.debug("Products retrieved from the database");
        logger.debug("Product ids: {}", () -> products.stream().map(Products::getId).toList());
        return mapper.entityListToDto(products);
    }

    public Optional<ProductsDto> getById(int id){
        Optional<Products> product = repository.findById(id);
        ProductsDto productDto = mapper.entityToDto(product.orElse(null));
        return Optional.ofNullable(productDto);
    }

    @Transactional
    public ProductsDto addProduct(ProductCreateDto dto){
        Products product = mapper.createDtoToEntity(dto);
        Products savedProduct = repository.save(product);
        return mapper.entityToDto(savedProduct);
    }

    @Transactional
    public ProductsDto updateProduct(int id, ProductsDto dto) { // Принимаем id как параметр
        Optional<Products> optional = repository.findById(id);
        if (optional.isPresent()) {
            Products product = mapper.dtoToEntity(dto); // Маппинг DTO в сущность
            product.setId(id); // Убедитесь, что id сохраняется (если нужно)
            Products savedProduct = repository.save(product); // Сохраняем обновленную сущность
            return mapper.entityToDto(savedProduct); // Возвращаем DTO
        }
        throw new OnlineGardenShopResourceNotFoundException(String.format("Product with id %s not found", id));
    }


    @Transactional
    public void deleteProduct(int id){
        repository.deleteById(id);
    }

}
