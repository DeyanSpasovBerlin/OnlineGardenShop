package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.UsersDto;
import finalproject.onlinegardenshop.dto.UsersUpdateDto;
import finalproject.onlinegardenshop.entity.Cart;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.entity.Users;
import finalproject.onlinegardenshop.entity.enums.UserRole;
import finalproject.onlinegardenshop.exception.OnlineGardenShopBadRequestException;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.OrdersMapper;
import finalproject.onlinegardenshop.mapper.UsersMapper;
import finalproject.onlinegardenshop.repository.CartItemsRepository;
import finalproject.onlinegardenshop.repository.CartRepository;
import finalproject.onlinegardenshop.repository.OrdersRepository;
import finalproject.onlinegardenshop.repository.UsersRepository;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UsersService {

    private static Logger logger = LogManager.getLogger(UsersService.class);

    private final UsersRepository repository;
    private final UsersMapper mapper;
    private final CartRepository cartRepository;
    private final OrdersRepository ordersRepository;
    private final OrdersMapper ordersMapper;
    private final CartItemsRepository cartItemsRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UsersService(UsersRepository repository,
                        UsersMapper mapper,
                        CartRepository cartRepository,
                        OrdersRepository ordersRepository,
                        OrdersMapper ordersMapper,
                        CartItemsRepository cartItemsRepository,
                        PasswordEncoder encoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.cartRepository = cartRepository;
        this.ordersRepository = ordersRepository;
        this.ordersMapper = ordersMapper;
        this.cartItemsRepository = cartItemsRepository;
        this.encoder = encoder;
    }

    public Page<UsersDto> getAllUsersSorted(Pageable pageable, String sortBy, String direction) {
        Sort sort = pageable.getSort();
        if (sortBy != null && direction != null) {
            String[] sortFields = sortBy.split(",");
            String[] directions = direction.split(",");
            List<Sort.Order> orders = new ArrayList<>();
            for (int i = 0; i < sortFields.length; i++) {
                String field = sortFields[i].trim();
                Sort.Direction dir = Sort.Direction.ASC;
                if (i < directions.length && directions[i].equalsIgnoreCase("desc")) {
                    dir = Sort.Direction.DESC;
                }
                orders.add(new Sort.Order(dir, field));
            }
            sort = Sort.by(orders);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<Users> usersPage = repository.findAll(pageable);
        return usersPage.map(mapper::entityToDto);
    }

    public UsersDto getUsersByIdForAdmin(Integer id) {
        Optional<Users> optional = repository.findById(id);
        if (optional.isPresent()) {
            UsersDto found = mapper.entityToDto(optional.get()) ;
            return found;
        }
        throw new OnlineGardenShopResourceNotFoundException("User with id = " + id + " not found in database");
    }

    public UsersDto getUsersById() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users authorizedUser = repository.findByEmail((String) auth.getPrincipal()).get();
        Integer authorizedUserId = authorizedUser.getId();
        return  mapper.entityToDto(authorizedUser) ;
    }

    public  List<UsersDto> findByFirstNameAndLastName(String firstName,String lastName){
        List<Users> managers = repository.findByFirstNameAndLastName(firstName,lastName);
        return mapper.entityListToDto(managers);
    }

    public List<UsersDto> findFirstLetterFromFirstNameAndFirstLetterFromLastName(
            String firstName,String lastName){
        List<Users> users = repository.findByFirstLetterFromFirstNameAndFirstLetterFromLastName(
                firstName,lastName);
        return mapper.entityListToDto(users);
    }

    @Transactional
    public UsersDto registerUser(UsersDto usersDto) {
        logger.debug("Registering new user with email: {}", usersDto.getEmail());

        Optional<Users> existingUser = repository.findByEmail(usersDto.getEmail());
        if (existingUser.isPresent()) {
            throw new OnlineGardenShopBadRequestException("Email is already in use");
        }
        Users user = mapper.dtoToEntity(usersDto);
        user.setRole(UserRole.CLIENT);
        user.setPassword(encoder.encode(usersDto.getPassword()));
        Users savedUser = repository.save(user);
        logger.info("User registered successfully: {}", savedUser.getEmail());
        return mapper.entityToDto(savedUser);
    }

    @Transactional
    public UsersDto updatedUsersByAdmin(Integer id, @Valid UsersUpdateDto usersDto) {
        Optional<Users> optionalId = repository.findById(id);
        if(optionalId.isPresent()){
            Users userToUpdate = optionalId.get();
            if (usersDto.getFirstName() != null) userToUpdate.setFirstName(usersDto.getFirstName());
            if (usersDto.getLastName() != null) userToUpdate.setLastName(usersDto.getLastName());
            if (usersDto.getPhone() != null) userToUpdate.setPhone(usersDto.getPhone());
            Users savedUser = repository.save(userToUpdate);
            return mapper.entityToDto(savedUser);
        }
        throw new OnlineGardenShopResourceNotFoundException("User with id = " + usersDto.getId() + " not found in database");
}

@Transactional
public UsersDto updatedUsersByUser(@Valid UsersUpdateDto usersUpdateDto) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Users authorizedUser = repository.findByEmail((String) auth.getPrincipal()).get();
    Integer authorizedUserId = authorizedUser.getId();

        if (usersUpdateDto.getFirstName() != null) authorizedUser.setFirstName(usersUpdateDto.getFirstName());
        if (usersUpdateDto.getLastName() != null) authorizedUser.setLastName(usersUpdateDto.getLastName());
        if (usersUpdateDto.getPhone() != null) authorizedUser.setPhone(usersUpdateDto.getPhone());
        Users savedUser = repository.save(authorizedUser);
        return mapper.entityToDto(savedUser);
}

        @Transactional
    public void deleteUserByAdmin(Integer userId){

        Optional<Users> userForDelete = repository.findById(userId);
        if (userForDelete.isPresent()) {
            Optional<Cart> cart = cartRepository.findByUsersId(userId);
            if (cart.isPresent()) {
                cartItemsRepository.deleteByCartId(cart.get().getId());
                cartRepository.delete(cart.get());
            }

            List<Orders> orders = ordersRepository.findByUsersId(userId);
            if(!orders.isEmpty()){
                for (Orders o : orders){
                    o.setUsers(null);
                    o.setDeletedUserId(-userId);
                    o.setDeliveryAddress(null);
                    o.setContactPhone(null);
                    ordersRepository.save(o);
                }
            }

            repository.deleteById(userId);
        }else {
            throw new OnlineGardenShopResourceNotFoundException("User with id = " + userId + " not found in database!");
        }
    }

    @Transactional
    public void deleteUserByUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users authorizedUser = repository.findByEmail((String) auth.getPrincipal()).get();
        Integer authorizedUserId = authorizedUser.getId();
            Optional<Cart> cart = cartRepository.findByUsersId(authorizedUserId);
            if (cart.isPresent()) {
                cartItemsRepository.deleteByCartId(cart.get().getId());
                cartRepository.delete(cart.get());
            }
            List<Orders> orders = ordersRepository.findByUsersId(authorizedUserId);
            if(!orders.isEmpty()){
                for (Orders o : orders){
                    o.setUsers(null);
                    o.setDeletedUserId(-authorizedUserId);
                    o.setDeliveryAddress(null);
                    o.setContactPhone(null);
                    ordersRepository.save(o);
                }
            }

            repository.deleteById(authorizedUserId);
    }

    @Transactional
    public UsersDto createUser(UsersDto userDto) {
        Users entity = mapper.dtoToEntity(userDto);
        entity.setPassword(encoder.encode(entity.getPassword()));
        Users user = repository.save(entity);
        return mapper.entityToDto(user);
    }

    public Optional<Users> getByLogin(String login) {
        return repository.findByEmail(login);
    }

    @Transactional
    public void save(Users user) {
        repository.save(user);
    }

}

