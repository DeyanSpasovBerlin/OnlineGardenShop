package finalproject.onlinegardenshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalproject.onlinegardenshop.config.SecurityConfig;
import finalproject.onlinegardenshop.dto.ProductsDto;
import finalproject.onlinegardenshop.dto.ProductCreateDto;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.security.JwtProvider;
import finalproject.onlinegardenshop.service.ProductsService;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductsController.class)
@Import(SecurityConfig.class)
class ProductsControllerTest {

    @MockitoBean
    private ProductsService service;
    @MockitoBean
    private JwtProvider jwtProvider;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAll_shouldReturnListOfProducts() throws Exception {
        ProductsDto product = new ProductsDto();
        product.setId(1);
        product.setName("Laptop");

        when(service.getAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetById_shouldReturnProduct() throws Exception {
        int id = 1;
        ProductsDto product = new ProductsDto();
        product.setId(id);
        product.setName("Smartphone");

        when(service.getById(id)).thenReturn(product);

        mockMvc.perform(get("/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Smartphone"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetById_shouldReturnNotFoundForNonExistentId() throws Exception {
        int id = 999;
        when(service.getById(id)).thenThrow(new OnlineGardenShopResourceNotFoundException("Product not found"));

        mockMvc.perform(get("/products/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddProduct_shouldReturnCreatedProduct() throws Exception {
        ProductCreateDto input = new ProductCreateDto();
        input.setName("T-shirt");
        input.setCategory("Clothing");
        input.setDescription("Soft cotton T-shirt");
        input.setPrice(20.0);
        input.setDiscountPrice(15.0);
        input.setImageUrl("http://example.com/tshirt.jpg");

        ProductsDto created = new ProductsDto();
        created.setId(1);
        created.setName("T-shirt");

        when(service.addProduct(any(ProductCreateDto.class))).thenReturn(created);

        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("T-shirt"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddProduct_shouldReturnBadRequestForInvalidProduct() throws Exception {
        ProductCreateDto input = new ProductCreateDto();
        input.setName(" ");
        input.setCategory("Clothing");
        input.setDescription("Soft cotton T-shirt");
        input.setPrice(-20.0);
        input.setDiscountPrice(15.0);
        input.setImageUrl("http://example.com/tshirt.jpg");

        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateProduct_shouldReturnUpdatedProduct() throws Exception {
        int id = 1;
        ProductsDto updated = new ProductsDto();
        updated.setId(id);
        updated.setName("Updated Product");

        when(service.updateProduct(eq(id), any(ProductsDto.class))).thenReturn(updated);

        mockMvc.perform(put("/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateProduct_shouldReturnBadRequestForInvalidProduct() throws Exception {
        int id = 1;
        ProductsDto invalidProduct = new ProductsDto();
        invalidProduct.setId(id);
        invalidProduct.setName(""); // Invalid name

        mockMvc.perform(put("/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteProduct_shouldReturnOk() throws Exception {
        int id = 1;
        doNothing().when(service).deleteProduct(id);

        mockMvc.perform(delete("/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Product with id " + id + " was successfully deleted"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteProduct_shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        int id = 999;
        doThrow(new OnlineGardenShopResourceNotFoundException("Product with id " + id + " not found"))
                .when(service).deleteProduct(id);

        mockMvc.perform(delete("/products/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetFilteredProducts_shouldReturnFilteredProducts() throws Exception {
            mockMvc.perform(get("/products/sort")
                        .param("category", "Electronics")
                        .param("priceRange", "100-500"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetDealOfTheDay_shouldReturnProduct() throws Exception {
        ProductsDto dealOfTheDay = new ProductsDto();
        dealOfTheDay.setName("Super Discounted Product");

        when(service.getDealOfTheDay()).thenReturn(dealOfTheDay);

        mockMvc.perform(get("/products/deal-of-the-day"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Super Discounted Product"));
    }
}

