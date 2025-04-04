package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.ProductCreateDto;
import finalproject.onlinegardenshop.dto.ProductsDto;
import finalproject.onlinegardenshop.service.ProductsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Products Controller", description = "REST API to manage products in the app")
public class ProductsController {

    private final ProductsService service;

    @Autowired
    public ProductsController(ProductsService service) {
        this.service = service;
    }


    @GetMapping("/all")
    @Operation(summary="Returns a list of all available products")
    public ResponseEntity<List<ProductsDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns a specific product by its id")
    public ResponseEntity<ProductsDto> getById(@PathVariable int id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/sort")
    @Operation(summary = "Returns a product or a list of products based on certain filtering/sorting parameter(s)")
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

        return ResponseEntity.ok(service.getFilteredProducts(category, minPrice, maxPrice, discount, sorting));
    }

    @GetMapping("deal-of-the-day")
    @Operation(summary = "Returns a product with the highest discount")
    public ResponseEntity<ProductsDto> getDealOfTheDay() {
        return ResponseEntity.ok(service.getDealOfTheDay());
    }

    @PostMapping("/add")
    @Operation(summary = "Adds a new product")
    public ResponseEntity<ProductsDto> add(@RequestBody @Valid ProductCreateDto product) {
        return new ResponseEntity<>(service.addProduct(product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Introduces desired changes to a product selected by its id")
    public ResponseEntity<ProductsDto> update(@PathVariable int id, @RequestBody @Valid ProductsDto product) {
        return ResponseEntity.ok(service.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a product selected by its id")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}

