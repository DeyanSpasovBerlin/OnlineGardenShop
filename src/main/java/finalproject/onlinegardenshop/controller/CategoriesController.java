package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.CategoriesDto;
import finalproject.onlinegardenshop.dto.CategoryCreateDto;
import finalproject.onlinegardenshop.service.CategoriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Validated
@Tag(name = "Categories Controller", description = "REST API to manage categories of products in the app")
public class CategoriesController {

    private final CategoriesService service;

    @Autowired
    public CategoriesController(CategoriesService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Returns a list of all available categories")
    public ResponseEntity<List<CategoriesDto>> getAll(){
        List<CategoriesDto> categories = service.getAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns a category by its id")
    public ResponseEntity<CategoriesDto> getById(@PathVariable int id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Adds a new category")
    public ResponseEntity<CategoriesDto> add(@RequestBody @Valid CategoryCreateDto category){
        CategoriesDto created = service.addCategory(category);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Introduces desired changes in the existing category selected by its id")
    public ResponseEntity<CategoriesDto> update(@PathVariable int id, @RequestParam
    @Pattern(regexp = "^[A-Za-zÜÄÖüäö'\\-., ]{1,255}$",
             message = "{validation.categories.name}")
    String name)
    {
        CategoriesDto updated = service.changeCategory(id, name);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a category selected by id")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.deleteCategory(id);
        return ResponseEntity.notFound().build();
    }

}
