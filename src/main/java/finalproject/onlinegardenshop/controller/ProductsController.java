package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.ProductCreateDto;
import finalproject.onlinegardenshop.dto.ProductDto;
import finalproject.onlinegardenshop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final ProductService service;

    @Autowired
    public ProductsController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAll(){
        List<ProductDto> products = service.getAll();
        if(!products.isEmpty()){
            return new ResponseEntity<>(products, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable int id){
        Optional<ProductDto> product = service.getById(id);
        if(product.isPresent()){
            return new ResponseEntity<>(product.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/add")
    public ResponseEntity<ProductDto> add(@RequestBody @Valid ProductCreateDto product){
        ProductDto created = service.addProduct(product);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable int id, @RequestBody @Valid ProductDto product){
        ProductDto updated = service.updateProduct(product);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id){
        service.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
