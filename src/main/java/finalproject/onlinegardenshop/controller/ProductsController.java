package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.ProductCreateDto;
import finalproject.onlinegardenshop.dto.ProductsDto;
import finalproject.onlinegardenshop.service.ProductsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@Validated
public class ProductsController {

    private final ProductsService service;

    @Autowired
    public ProductsController(ProductsService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductsDto>> getAll(){
        List<ProductsDto> products = service.getAll();
        if(!products.isEmpty()){
            return new ResponseEntity<>(products, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductsDto> getById(@PathVariable int id){
        Optional<ProductsDto> product = service.getById(id);
        if(product.isPresent()){
            return new ResponseEntity<>(product.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<ProductsDto>> getFilteredProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean discount,
            @RequestParam(required = false, defaultValue = "name") String sort) {

        Sort sorting = switch (sort) {
            case "priceAsc" -> Sort.by(Sort.Order.asc("price"));
            case "priceDesc" -> Sort.by(Sort.Order.desc("price"));
            case "dateAsc" -> Sort.by(Sort.Order.asc("createdAt"));
            case "dateDesc" -> Sort.by(Sort.Order.desc("createdAt"));
            case "nameDesc" -> Sort.by(Sort.Order.desc("name"));
            default -> Sort.by(Sort.Order.asc("name"));
        };

        List<ProductsDto> products = service.getFilteredProducts(category, minPrice, maxPrice, discount, sorting);
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @PostMapping("/add")
    public ResponseEntity<ProductsDto> add(@RequestBody @Valid ProductCreateDto product){
        ProductsDto created = service.addProduct(product);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductsDto> update(@PathVariable int id, @RequestBody @Valid ProductsDto product){
        ProductsDto updated = service.updateProduct(id, product);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id){
        service.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

