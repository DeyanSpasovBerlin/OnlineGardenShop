package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.CategoriesDto;
import finalproject.onlinegardenshop.dto.CategoryCreateDto;
import finalproject.onlinegardenshop.entity.Categories;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.CategoriesMapper;
import finalproject.onlinegardenshop.repository.CategoriesRepository;
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
public class CategoriesService {

    private final static Logger logger = LogManager.getLogger(CategoriesService.class);

    private final CategoriesRepository repository;
    private final ProductsRepository productsRepository;
    private final CategoriesMapper mapper;

    @Autowired
    public CategoriesService(CategoriesRepository repository,
                             ProductsRepository productsRepository,
                             CategoriesMapper mapper)
    {
        this.repository = repository;
        this.productsRepository = productsRepository;
        this.mapper = mapper;
    }

    public List<CategoriesDto> getAll() {
        List<Categories> categories = repository.findAll();
        if (categories.isEmpty()) {
            throw new OnlineGardenShopResourceNotFoundException("Categories not found");
        }
        logger.debug("Categories retrieved from database");
        logger.debug("Category ids: {}", ()->categories.stream().map(Categories::getId).toList());
        return mapper.entityListToDto(categories);
    }

    public CategoriesDto getById(int id) {
        return repository.findById(id).map(mapper::entityToDto)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Category with id " + id + " not found"));
    }

    @Transactional
    public CategoriesDto addCategory(CategoryCreateDto dto) {
        Categories category = mapper.createDtoToEntity(dto);
        Categories savedCategory = repository.save(category);
        return mapper.entityToDto(savedCategory);
    }

    @Transactional
    public CategoriesDto changeCategory(int id, String name) {
        Optional<Categories> optional = repository.findById(id);
        if (optional.isPresent()) {
            Categories category = optional.get();
            category.setName(name);
            Categories saved = repository.save(category);
            return mapper.entityToDto(saved);
        }
        throw new OnlineGardenShopResourceNotFoundException("Category with id " + id + " not found");
    }

    @Transactional
    public void deleteCategory(int id) {
        Categories category = repository.findById(id)
                .orElseThrow(() -> new OnlineGardenShopResourceNotFoundException("Category with id " + id + " not found"));
        productsRepository.updateCategoryToNull(id);
        repository.deleteById(id);
    }

}


