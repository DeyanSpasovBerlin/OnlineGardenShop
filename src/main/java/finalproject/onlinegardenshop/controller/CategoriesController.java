package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.CategoriesDto;
import finalproject.onlinegardenshop.dto.CategoryCreateDto;
import finalproject.onlinegardenshop.service.CategoriesService;
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
public class CategoriesController {

    private final CategoriesService service;

    @Autowired
    public CategoriesController(CategoriesService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CategoriesDto>> getAll(){
        List<CategoriesDto> categories = service.getAll();
        if(!categories.isEmpty()){
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriesDto> getById(@PathVariable int id){
        Optional<CategoriesDto> category = service.getById(id);
        if(category.isPresent()){
            return new ResponseEntity<>(category.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<CategoriesDto> add(@RequestBody @Valid CategoryCreateDto category){
        CategoriesDto created = service.addCategory(category);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriesDto> update(@PathVariable int id, @RequestParam
    @Pattern(regexp = "^[A-Za-zÜÄÖüäö'\\-., ]{1,255}$",
             message = "{validation.categories.name}")
    String name)
    {
        CategoriesDto updated = service.changeCategory(id, name);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id){
        service.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
