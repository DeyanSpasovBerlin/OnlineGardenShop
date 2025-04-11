package finalproject.onlinegardenshop.specification;

import finalproject.onlinegardenshop.entity.Products;
import org.springframework.data.jpa.domain.Specification;

public class ProductsSpecification {

    public static Specification<Products> fromParam(String key, String value) {
        return switch (key) {
            case "category" -> hasCategory(value);
            case "minPrice" -> minPrice(Double.valueOf(value));
            case "maxPrice" -> maxPrice(Double.valueOf(value));
            case "discount" -> Boolean.parseBoolean(value)
                    ? hasDiscount()
                    : noDiscount();
            default -> null;
        };
    }

    public static Specification<Products> hasCategory(String category) {
        return (root, query, cb) -> cb.equal(root.get("category").get("name"), category);
    }

    public static Specification<Products> minPrice(Double minPrice) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Products> maxPrice(Double maxPrice) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Products> hasDiscount() {
        return (root, query, cb) -> cb.gt(root.get("discountPrice"), 0);
    }

    public static Specification<Products> noDiscount() {
        return (root, query, cb) -> cb.isNull(root.get("discountPrice"));
    }

}
