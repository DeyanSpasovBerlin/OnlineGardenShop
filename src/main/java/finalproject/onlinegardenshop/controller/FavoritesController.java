package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.FavoritesDto;
import finalproject.onlinegardenshop.service.FavoritesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/favorites")
@Tag(name = "Favorites Controller", description = "REST API to manage favorite products in the app")
public class FavoritesController {
    private final FavoritesService service;

    public FavoritesController(FavoritesService service) {
        this.service = service;
    }

    @GetMapping("/all")
    @Operation(summary = "Returns a list of favorite products of a user")
    public ResponseEntity<List<FavoritesDto>> getAllFavorites() {
        return ResponseEntity.ok(service.getAllFavorites());
    }

    @PostMapping
    @Operation(summary = "Adds a product to favorites of a user")
    public ResponseEntity<FavoritesDto> saveFavorite(@RequestBody FavoritesDto dto) {
        return ResponseEntity.ok(service.saveFavorite(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a favorite product by its id")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Integer id) {
        service.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }
}

