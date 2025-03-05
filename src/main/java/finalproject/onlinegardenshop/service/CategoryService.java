package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.CategoryCreateDto;
import finalproject.onlinegardenshop.dto.CategoryDto;
import finalproject.onlinegardenshop.entity.Categories;
import finalproject.onlinegardenshop.mapper.CategoryMapper;
import finalproject.onlinegardenshop.repository.CategoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private static Logger logger = LogManager.getLogger(CategoryService.class);

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Autowired
    public CategoryService(CategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CategoryDto> getAll() {
      List<Categories> categories = repository.findAll();
      logger.debug("Categories retrieved from database");
      logger.debug("Category ids: {}", ()->categories.stream().map(Categories::getId).toList());
      return mapper.entityListToDto(categories);
    }

    public Optional<CategoryDto> getById(int id) {
        Optional<Categories> category = repository.findById(id);
        CategoryDto dto = mapper.entityToDto(category.orElse(null));
        return Optional.ofNullable(dto);
    }

    public CategoryDto addCategory(CategoryCreateDto dto) {
        Categories category = mapper.createDtoToEntity(dto);
        Categories savedCategory = repository.save(category);
        return mapper.entityToDto(savedCategory);
    }







}
