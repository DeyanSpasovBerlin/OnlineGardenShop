package finalproject.onlinegardenshop.specification;

import finalproject.onlinegardenshop.entity.Products;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductsSpecificationTest {

    @Test
    void fromParam_category_returnsCategorySpec() {
        Specification<Products> spec = ProductsSpecification.fromParam("category", "Electronics");

        assertNotNull(spec);

        Root<Products> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        var categoryPath = mock(jakarta.persistence.criteria.Path.class);
        var category = mock(jakarta.persistence.criteria.Path.class);

        when(root.get("category")).thenReturn(category);
        when(category.get("name")).thenReturn(categoryPath);

        Predicate expectedPredicate = mock(Predicate.class);
        when(cb.equal(categoryPath, "Electronics")).thenReturn(expectedPredicate);

        Predicate result = spec.toPredicate(root, query, cb);
        assertEquals(expectedPredicate, result);
    }

    @Test
    void fromParam_minPrice_returnsMinPriceSpec() {
        Specification<Products> spec = ProductsSpecification.fromParam("minPrice", "100");

        assertNotNull(spec);

        Root<Products> root = mock(Root.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);

        var pricePath = mock(jakarta.persistence.criteria.Path.class);
        when(root.get("price")).thenReturn(pricePath);

        Predicate expected = mock(Predicate.class);
        when(cb.greaterThanOrEqualTo(pricePath, 100.0)).thenReturn(expected);

        Predicate result = spec.toPredicate(root, query, cb);
        assertEquals(expected, result);
    }

    @Test
    void fromParam_maxPrice_returnsMaxPriceSpec() {
        Specification<Products> spec = ProductsSpecification.fromParam("maxPrice", "500");

        assertNotNull(spec);

        Root<Products> root = mock(Root.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);

        var pricePath = mock(jakarta.persistence.criteria.Path.class);
        when(root.get("price")).thenReturn(pricePath);

        Predicate expected = mock(Predicate.class);
        when(cb.lessThanOrEqualTo(pricePath, 500.0)).thenReturn(expected);

        Predicate result = spec.toPredicate(root, query, cb);
        assertEquals(expected, result);
    }

    @Test
    void fromParam_discountTrue_returnsHasDiscountSpec() {
        Specification<Products> spec = ProductsSpecification.fromParam("discount", "true");

        assertNotNull(spec);

        Root<Products> root = mock(Root.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);

        var discountPath = mock(jakarta.persistence.criteria.Path.class);
        when(root.get("discountPrice")).thenReturn(discountPath);

        Predicate expected = mock(Predicate.class);
        when(cb.gt(discountPath, 0)).thenReturn(expected);

        Predicate result = spec.toPredicate(root, query, cb);
        assertEquals(expected, result);
    }

    @Test
    void fromParam_discountFalse_returnsNoDiscountSpec() {
        Specification<Products> spec = ProductsSpecification.fromParam("discount", "false");

        assertNotNull(spec);

        Root<Products> root = mock(Root.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);

        var discountPath = mock(jakarta.persistence.criteria.Path.class);
        when(root.get("discountPrice")).thenReturn(discountPath);

        Predicate expected = mock(Predicate.class);
        when(cb.isNull(discountPath)).thenReturn(expected);

        Predicate result = spec.toPredicate(root, query, cb);
        assertEquals(expected, result);
    }

    @Test
    void fromParam_unknownKey_returnsNull() {
        Specification<Products> spec = ProductsSpecification.fromParam("unknown", "value");
        assertNull(spec);
    }
}
