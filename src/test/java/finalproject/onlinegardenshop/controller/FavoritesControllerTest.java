package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.FavoritesDto;
import finalproject.onlinegardenshop.service.FavoritesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class FavoritesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FavoritesService service;

    @InjectMocks
    private FavoritesController controller;

    private FavoritesDto favoriteDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        favoriteDto = new FavoritesDto();
        favoriteDto.setId(1);
    }

    @Test
    void getAllFavorites_ShouldReturnFavoritesList() throws Exception {
        when(service.getAllFavorites()).thenReturn(List.of(favoriteDto));

        mockMvc.perform(get("/favorites/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(service, times(1)).getAllFavorites();
    }

    @Test
    void saveFavorite_ShouldReturnSavedFavorite() throws Exception {
        when(service.saveFavorite(any())).thenReturn(favoriteDto);

        mockMvc.perform(post("/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(favoriteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(service, times(1)).saveFavorite(any());
    }

    @Test
    void deleteFavorite_ShouldReturnNoContent() throws Exception {
        doNothing().when(service).deleteFavorite(1);

        mockMvc.perform(delete("/favorites/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteFavorite(1);
    }
}