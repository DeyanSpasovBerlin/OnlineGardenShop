package finalproject.onlinegardenshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalproject.onlinegardenshop.dto.FavoritesDto;
import finalproject.onlinegardenshop.service.FavoritesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FavoritesController.class)
class FavoritesControllerTest {

    @MockitoBean
    private FavoritesService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getAllFavorites() throws Exception {
        mockMvc.perform(get("/favorites/all").contentType("application/json"))
                .andExpect(status().isOk());
        verify(service).getAllFavorites();
    }

    @Test
    void saveFavorite() throws Exception {
        FavoritesDto favorite = new FavoritesDto();
        when(service.saveFavorite(any(FavoritesDto.class))).thenReturn(favorite);

        MvcResult mvcResult = mockMvc.perform(post("/favorites")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(favorite)))
                .andExpect(status().isOk())
                .andReturn();
        verify(service).saveFavorite(any(FavoritesDto.class));

        String json = mvcResult.getResponse().getContentAsString();
        assertEquals(mapper.writeValueAsString(favorite), json);
    }

    @Test
    void deleteFavorite() throws Exception {
        mockMvc.perform(delete("/favorites/1").contentType("application/json"))
                .andExpect(status().isNoContent());
        verify(service).deleteFavorite(1);
    }
}