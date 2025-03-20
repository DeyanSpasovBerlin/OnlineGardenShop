package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.entity.Users;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.repository.ProductsRepository;
import finalproject.onlinegardenshop.repository.UsersRepository;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrdersMapperInjector {
    /*
    идея делаем спомогательной клас, которой инжектируем в OrdersMaper. OrdersMaper не может директно достать product namе.
     productId можно взять директно из OrderItem,но product namе там нету. Поетому инжектируем с анотации @Named("productNameFromId")
     только с етой анотации мапер понимает что ето поле надо включит в переобразования
     */

    private ProductsRepository productsRepository;
    private UsersRepository usersRepository;

    @Autowired
    public OrdersMapperInjector(ProductsRepository productsRepository, UsersRepository usersRepository) {
        this.productsRepository = productsRepository;
        this.usersRepository = usersRepository;
    }

    public OrdersMapperInjector(ProductsRepository productsRepository) {
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

    @Named("userFirstName")
    public String userFirstName(Integer userId){
        Optional<Users> user = usersRepository.findById(userId);
        if(user.isPresent()){
            return user != null ? user.get().getFirstName() : null;
        }
        throw new OnlineGardenShopResourceNotFoundException("No user with "+userId+" in data base");
    }

    @Named("userLasttName")
    public String userLasttName(Integer userId){
        Optional<Users> user = usersRepository.findById(userId);
        if(user.isPresent()){
            return user != null ? user.get().getLastName() : null;
        }
        throw new OnlineGardenShopResourceNotFoundException("No user with "+userId+" in data base");
    }

}
