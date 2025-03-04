package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.ProductCreateDto;
import finalproject.onlinegardenshop.dto.ProductDto;
import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.ProductMapper;
import finalproject.onlinegardenshop.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository repository;
    private static final Logger logger = LogManager.getLogger(ProductService.class);
    private final ProductMapper mapper;

    @Autowired
    public ProductService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ProductDto> getAll(){
        List<Products> products = repository.findAll();
        logger.debug("Products retrieved from the database");
        logger.debug("Product ids: {}", () -> products.stream().map(Products::getId).toList());
        return mapper.entityListToDto(products);
    }

    public Optional<ProductDto> getById(int id){
        Optional<Products> product = repository.findById(id);
        ProductDto productDto = mapper.entityToDto(product.orElse(null));
        return Optional.ofNullable(productDto);
    }

    @Transactional
    public ProductDto addProduct(ProductCreateDto dto){
        Products product = mapper.createDtoToEntity(dto);
        Products savedProduct = repository.save(product);
        return mapper.entityToDto(savedProduct);
    }

    @Transactional
    public ProductDto updateProduct(ProductDto dto){
        int id = dto.getId();
        Optional<Products> optional = repository.findById(id);
        if(optional.isPresent()){
            Products product = mapper.dtoToEntity(dto);
            Products savedProduct = repository.save(product);
            return mapper.entityToDto(savedProduct);
        }
        throw new OnlineGardenShopResourceNotFoundException(String.format("Product with id %s not found", id));
    }

    @Transactional
    public void deleteProduct(int id){
        repository.deleteById(id);
    }

}
