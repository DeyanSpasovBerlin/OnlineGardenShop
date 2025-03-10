package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.CategoriesDto;
import finalproject.onlinegardenshop.dto.CategoryCreateDto;
import finalproject.onlinegardenshop.entity.Categories;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.CategoriesMapper;
import finalproject.onlinegardenshop.repository.CategoriesRepository;
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
    private final CategoriesMapper mapper;

    @Autowired
    public CategoriesService(CategoriesRepository repository, CategoriesMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CategoriesDto> getAll() {
        List<Categories> categories = repository.findAll();
        logger.debug("Categories retrieved from database");
        logger.debug("Category ids: {}", ()->categories.stream().map(Categories::getId).toList());
        return mapper.entityListToDto(categories);
    }

    public Optional<CategoriesDto> getById(int id) {
        Optional<Categories> category = repository.findById(id);
        CategoriesDto dto = mapper.entityToDto(category.orElse(null));
        return Optional.ofNullable(dto);
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
        repository.deleteById(id);
    }

}

