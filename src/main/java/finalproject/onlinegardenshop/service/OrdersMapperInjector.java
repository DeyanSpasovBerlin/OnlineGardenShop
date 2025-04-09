package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.repository.ProductsRepository;
import finalproject.onlinegardenshop.repository.UsersRepository;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrdersMapperInjector {

    private ProductsRepository productsRepository;
    private UsersRepository usersRepository;

    @Autowired
    public OrdersMapperInjector(ProductsRepository productsRepository, UsersRepository usersRepository) {
        this.productsRepository = productsRepository;
        this.usersRepository = usersRepository;
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
