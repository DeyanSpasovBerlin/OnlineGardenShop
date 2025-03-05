package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.CategoryCreateDto;
import finalproject.onlinegardenshop.dto.CategoryDto;
import finalproject.onlinegardenshop.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@Validated
public class CategoryController {

    private final CategoryService service;

    @Autowired
    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll(){
        List<CategoryDto> categories = service.getAll();
        if(!categories.isEmpty()){
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getById(@PathVariable int id){
        Optional<CategoryDto> category = service.getById(id);
        if(category.isPresent()){
            return new ResponseEntity<>(category.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> add(@RequestBody @Valid CategoryCreateDto category){
        CategoryDto created = service.addCategory(category);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable int id, @RequestParam
    @Pattern(regexp = "^[A-ZÜÄÖ][a-zA-Züäö'\\- ]{0,254}$",
            message = "{validation.categories.name}")
    String name)
    {
        CategoryDto updated = service.changeCategory(id, name);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id){
        service.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}

