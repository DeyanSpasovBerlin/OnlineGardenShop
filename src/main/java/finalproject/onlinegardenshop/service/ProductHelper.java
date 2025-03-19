package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.ProductsMapper;
import finalproject.onlinegardenshop.repository.ProductsRepository;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductHelper {
    /*
    идея делаем спомогательной клас, которой инжектируем в OrdersMaper. OrdersMaper не может директно достать product namе.
     productId можно взять директно из OrderItem,но product namе там нету. Поетому инжектируем с анотации @Named("productNameFromId")
     только с етой анотации мапер понимает что ето поле надо включит в переобразования
     */

    private ProductsRepository productsRepository;
    @Autowired
    public ProductHelper(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Named("productNameFromId")
    public String productNameFromId(Integer productId) {
        Optional<Products> product = productsRepository.findById(productId);
        if(product.isPresent()){
            return product != null ? product.get().getName() : null;
        }
        throw new OnlineGardenShopResourceNotFoundException("No product with "+productId+" in data base");
    }

}
