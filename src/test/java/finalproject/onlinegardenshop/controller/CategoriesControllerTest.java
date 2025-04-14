package finalproject.onlinegardenshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalproject.onlinegardenshop.config.SecurityConfig;
import finalproject.onlinegardenshop.dto.CategoriesDto;
import finalproject.onlinegardenshop.dto.CategoryCreateDto;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.repository.CategoriesRepository;
import finalproject.onlinegardenshop.security.JwtProvider;
import finalproject.onlinegardenshop.service.CategoriesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CategoriesController.class)
@Import(SecurityConfig.class)
class CategoriesControllerTest {

    @MockitoBean
    private CategoriesService service;
    @MockitoBean
    private JwtProvider jwtProvider;
    @MockitoBean
    private CategoriesRepository repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAll_shouldReturnListOfCategories() throws Exception {
        CategoriesDto category = new CategoriesDto();
        category.setId(1);
        category.setName("Electronics");

        when(service.getAll()).thenReturn(List.of(category));

        mockMvc.perform(get("/categories"))
                .andDo(print()) // Выводит тело ответа в консоль
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetById_shouldReturnCategory() throws Exception {
        int id = 1;
        CategoriesDto category = new CategoriesDto();
        category.setId(id);
        category.setName("Books");

        when(service.getById(id)).thenReturn(category);

        mockMvc.perform(get("/categories/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Books"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetById_shouldReturnNotFoundForNonExistentId() throws Exception {
        int id = 999;
        when(service.getById(id)).thenThrow(new OnlineGardenShopResourceNotFoundException("Category not found"));

        mockMvc.perform(get("/categories/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddCategory_shouldReturnCreatedCategory() throws Exception {
        CategoryCreateDto input = new CategoryCreateDto("Clothing");

        CategoriesDto created = new CategoriesDto();
        created.setId(1);
        created.setName("Clothing");

        when(service.addCategory(any(CategoryCreateDto.class))).thenReturn(created);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Clothing"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddCategory_shouldReturnBadRequestForInvalidName() throws Exception {
        CategoryCreateDto input = new CategoryCreateDto("!@#$%");

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateCategory_shouldReturnUpdatedCategory() throws Exception {
        int id = 1;
        String newName = "Home and Garden";

        CategoriesDto updated = new CategoriesDto();
        updated.setId(id);
        updated.setName(newName);

        when(service.changeCategory(eq(id), eq(newName))).thenReturn(updated);

        mockMvc.perform(put("/categories/{id}", id)
                        .param("name", newName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(newName));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateCategory_shouldReturnBadRequestForInvalidName() throws Exception {
        int id = 1;
        String invalidName = "###invalid###";

        mockMvc.perform(put("/categories/{id}", id)
                        .param("name", invalidName))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteCategory_shouldReturnNotFound() throws Exception {
        int id = 1;
        doNothing().when(service).deleteCategory(id);

        mockMvc.perform(delete("/categories/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteCategory_shouldReturnNotFoundWhenCategoryDoesNotExist() throws Exception {
        int id = 123;
        doThrow(new OnlineGardenShopResourceNotFoundException("Category with id " + id + " not found"))
                .when(service).deleteCategory(id);

        mockMvc.perform(delete("/categories/{id}", id))
                .andExpect(status().isNotFound());
    }

}
