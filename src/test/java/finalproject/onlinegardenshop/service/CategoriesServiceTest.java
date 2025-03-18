package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.CategoriesDto;
import finalproject.onlinegardenshop.dto.CategoryCreateDto;
import finalproject.onlinegardenshop.entity.Categories;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.CategoriesMapper;
import finalproject.onlinegardenshop.repository.CategoriesRepository;
import finalproject.onlinegardenshop.repository.ProductsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriesServiceTest {

    @Mock
    private CategoriesRepository categoriesRepository;

    @Mock
    private ProductsRepository productsRepository;

    @Mock
    private CategoriesMapper categoriesMapper;

    @InjectMocks
    private CategoriesService categoriesService;

    @Test
    void testGetAll() {
        // Подготовка данных
        Categories category = new Categories();
        category.setId(1);
        category.setName("Test Category");

        List<Categories> categories = List.of(category);
        CategoriesDto categoryDto = new CategoriesDto();
        categoryDto.setId(1);
        categoryDto.setName("Test Category");

        // Мокируем поведение репозитория и маппера
        when(categoriesRepository.findAll()).thenReturn(categories);
        when(categoriesMapper.entityListToDto(categories)).thenReturn(List.of(categoryDto));

        // Вызов метода
        List<CategoriesDto> result = categoriesService.getAll();

        // Проверки
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Category", result.get(0).getName());
    }

    @Test
    void testGetById() {
        // Подготовка данных
        Categories category = new Categories();
        category.setId(1);
        category.setName("Test Category");

        CategoriesDto categoryDto = new CategoriesDto();
        categoryDto.setId(1);
        categoryDto.setName("Test Category");

        // Мокируем поведение репозитория и маппера
        when(categoriesRepository.findById(1)).thenReturn(Optional.of(category));
        when(categoriesMapper.entityToDto(category)).thenReturn(categoryDto);

        // Вызов метода
        Optional<CategoriesDto> result = categoriesService.getById(1);

        // Проверки
        assertTrue(result.isPresent());
        assertEquals("Test Category", result.get().getName());
    }

    @Test
    void testAddCategory() {
        // Подготовка данных
        CategoryCreateDto categoryCreateDto = new CategoryCreateDto();
        categoryCreateDto.setName("New Category");

        Categories category = new Categories();
        category.setId(1);
        category.setName("New Category");

        CategoriesDto categoryDto = new CategoriesDto();
        categoryDto.setId(1);
        categoryDto.setName("New Category");

        // Мокируем поведение маппера и репозитория
        when(categoriesMapper.createDtoToEntity(categoryCreateDto)).thenReturn(category);
        when(categoriesRepository.save(category)).thenReturn(category);
        when(categoriesMapper.entityToDto(category)).thenReturn(categoryDto);

        // Вызов метода
        CategoriesDto result = categoriesService.addCategory(categoryCreateDto);

        // Проверки
        assertNotNull(result);
        assertEquals("New Category", result.getName());
    }

    @Test
    void testChangeCategory() {
        // Подготовка данных
        Categories category = new Categories();
        category.setId(1);
        category.setName("Old Category");

        CategoriesDto categoryDto = new CategoriesDto();
        categoryDto.setId(1);
        categoryDto.setName("New Category");

        // Мокируем поведение репозитория и маппера
        when(categoriesRepository.findById(1)).thenReturn(Optional.of(category));
        when(categoriesMapper.entityToDto(category)).thenReturn(categoryDto);

        // Вызов метода
        CategoriesDto result = categoriesService.changeCategory(1, "New Category");

        // Проверки
        assertNotNull(result);
        assertEquals("New Category", result.getName());
    }

    @Test
    void testChangeCategoryNotFound() {
        // Мокируем поведение репозитория
        when(categoriesRepository.findById(1)).thenReturn(Optional.empty());

        // Вызов метода и проверка исключения
        assertThrows(OnlineGardenShopResourceNotFoundException.class, () -> {
            categoriesService.changeCategory(1, "New Category");
        });
    }

    @Test
    void testDeleteCategory() {
        // Подготовка данных
        int categoryId = 1;

        // Мокируем поведение репозитория
        doNothing().when(productsRepository).updateCategoryToNull(categoryId);
        doNothing().when(categoriesRepository).deleteById(categoryId);

        // Вызов метода
        categoriesService.deleteCategory(categoryId);

        // Проверки
        verify(productsRepository, times(1)).updateCategoryToNull(categoryId);
        verify(categoriesRepository, times(1)).deleteById(categoryId);
    }
}

