package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.FavoritesDto;
import finalproject.onlinegardenshop.service.FavoritesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoritesController {
    private final FavoritesService service;

    public FavoritesController(FavoritesService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<FavoritesDto>> getAllFavorites() {
        return ResponseEntity.ok(service.getAllFavorites());
    }

    @PostMapping
    public ResponseEntity<FavoritesDto> saveFavorite(@RequestBody FavoritesDto dto) {
        return ResponseEntity.ok(service.saveFavorite(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Integer id) {
        service.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }
}

