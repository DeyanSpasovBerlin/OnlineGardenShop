package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.FavoriteDto;
import finalproject.onlinegardenshop.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService service;

    public FavoriteController(FavoriteService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<FavoriteDto>> getAllFavorites() {
        return ResponseEntity.ok(service.getAllFavorites());
    }

    @PostMapping
    public ResponseEntity<FavoriteDto> saveFavorite(@RequestBody FavoriteDto dto) {
        return ResponseEntity.ok(service.saveFavorite(dto));
    }

    //delete добавить
}

