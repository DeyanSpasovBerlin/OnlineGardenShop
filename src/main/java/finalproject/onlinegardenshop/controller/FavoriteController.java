package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.FavoriteDto;
import finalproject.onlinegardenshop.service.FavoriteService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService service;

    public FavoriteController(FavoriteService service) {
        this.service = service;
    }

    @GetMapping
    public List<FavoriteDto> getAllFavorites() {
        return service.getAllFavorites();
    }

    @GetMapping("/{id}")
    public FavoriteDto getFavoriteById(@PathVariable Integer id) {
        return service.getFavoriteById(id);
    }

    @PostMapping
    public FavoriteDto saveFavorite(@RequestBody FavoriteDto dto) {
        return service.saveFavorite(dto);
    }
}

