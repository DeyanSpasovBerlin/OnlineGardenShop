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
import org.springframework.security.access.AccessDeniedException;
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




//    public List<UsersDto> getAll(){
//        List<Users> users = repository.findAll();
//        logger.debug("Users retrieved from db");
//        logger.debug("user ids: {}", () -> users.stream().map(Users::getId).toList());
//        return mapper.entityListToDto(users);
//    }
    //***************************************
//        public Page<UsersDto> getSortedUsers(int page, int size, String sortBy, String direction) {
//            Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
//            Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
//
//            Page<Users> usersPage = repository.findAll(pageable);
//            return usersPage.map(mapper::entityToDto);
//        }

    public Page<UsersDto> getAllUsersSorted(Pageable pageable, String sortBy, String direction) {
        Sort sort = pageable.getSort();

        if (sortBy != null && direction != null) {
            String[] sortFields = sortBy.split(",");
            String[] directions = direction.split(",");

            List<Sort.Order> orders = new ArrayList<>();
            for (int i = 0; i < sortFields.length; i++) {
                String field = sortFields[i].trim();
                Sort.Direction dir = Sort.Direction.ASC; // по подразбиране

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
    //*********************************************************
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
        Users authorizedUser = repository.findByEmail((String) auth.getPrincipal()).get();//find user, who is authorized
        Integer authorizedUserId = authorizedUser.getId();//find userId, who is authorized
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

    // REST API from tex docs:
    //    1 •	Регистрация пользователя -> service
    @Transactional
    public UsersDto registerUser(UsersDto usersDto) {
        logger.debug("Registering new user with email: {}", usersDto.getEmail());
        // Check if email already exists
        Optional<Users> existingUser = repository.findByEmail(usersDto.getEmail());
        if (existingUser.isPresent()) {
            throw new OnlineGardenShopBadRequestException("Email is already in use");//400 Bad Request
            // or if Invalid input (missing fields, wrong format) -> "email": "Email is required"
        }
        // Convert DTO to entity and save to DB
        Users user = mapper.dtoToEntity(usersDto);
        user.setRole(UserRole.CLIENT); // Default role
        user.setPassword(encoder.encode(usersDto.getPassword()));// КОДИРАМЕ ПАРОЛАТА ПРЕДИ ЗАПИС
        Users savedUser = repository.save(user);
        logger.info("User registered successfully: {}", savedUser.getEmail());//201 Created
        return mapper.entityToDto(savedUser);
    }

    @Transactional
    public UsersDto updatedUsersByAdmin(Integer id, @Valid UsersUpdateDto usersDto) {
        Optional<Users> optionalId = repository.findById(id);
        if(optionalId.isPresent()){
            Users userToUpdate = optionalId.get();
            // если какаое-то поле не задано, задаем его вручную от старого ентити manualy set existing fields
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
    Users authorizedUser = repository.findByEmail((String) auth.getPrincipal()).get();//find user, who is authorized
    Integer authorizedUserId = authorizedUser.getId();//find userId, who is authorized
        // если какаое-то поле не задано, задаем его вручную от старого ентити manualy set existing fields
        if (usersUpdateDto.getFirstName() != null) authorizedUser.setFirstName(usersUpdateDto.getFirstName());
        if (usersUpdateDto.getLastName() != null) authorizedUser.setLastName(usersUpdateDto.getLastName());
        if (usersUpdateDto.getPhone() != null) authorizedUser.setPhone(usersUpdateDto.getPhone());
        Users savedUser = repository.save(authorizedUser);
        return mapper.entityToDto(savedUser);
}

    // REST API from tex docs:
    //4 •	Удаление учетной записи
        @Transactional
    public void deleteUserByAdmin(Integer userId){
        //✅ Общая идея: Users<-oneToOne<-Cart->oneToMany->CartItems;
        // Начинаем в обратном порядки: впервые del CartItemsр потом Cart и наконец Users
        //Users <- onetoMany<-Orders: впервые во всеф Orders которые делал етот User всавим userId=null.
        //Не del Orders  как вверх del Cart and CartItems, потому что мы хотим что бы все Orders хранилис,
        // даже если User del для статистики и судебных разбирательств. А Cart and CartItems привязанные к User
        // и их можно удалить вместе с ним. Здесь принцип таков- идем в обратную сторону по цепи relations преди да
        //стигнем до User  and del
        Optional<Users> userForDelete = repository.findById(userId);
        if (userForDelete.isPresent()) {
            // ✅ Step 1: Delete Cart Items first
            Optional<Cart> cart = cartRepository.findByUsersId(userId);
            if (cart.isPresent()) {
                cartItemsRepository.deleteByCartId(cart.get().getId()); // Delete cart items first
                cartRepository.delete(cart.get()); // Then delete the cart
            }
            // ✅ Step 2: Detach User from Orders
            List<Orders> orders = ordersRepository.findByUsersId(userId);
            if(!orders.isEmpty()){
                for (Orders o : orders){
                    o.setUsers(null);// Nullify the user reference
                    o.setDeletedUserId(-userId);// Store deleted user's ID as negative
                    o.setDeliveryAddress(null);//No personal data for deleted user
                    o.setContactPhone(null);//No personal data for deleted user
                    ordersRepository.save(o);
                }
            }
            // ✅ Step 3: Finally, delete the User
            repository.deleteById(userId);
        }else {
            throw new OnlineGardenShopResourceNotFoundException("User with id = " + userId + " not found in database!");
        }
    }

    @Transactional
    public void deleteUserByUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users authorizedUser = repository.findByEmail((String) auth.getPrincipal()).get();//find user, who is authorized
        Integer authorizedUserId = authorizedUser.getId();//find userId, who is authorized
//        Optional<Users> userForDelete = repository.findById(authorizedUserId);
//        if (userForDelete.isPresent()) {
            // ✅ Step 1: Delete Cart Items first
            Optional<Cart> cart = cartRepository.findByUsersId(authorizedUserId);
            if (cart.isPresent()) {
                cartItemsRepository.deleteByCartId(cart.get().getId()); // Delete cart items first
                cartRepository.delete(cart.get()); // Then delete the cart
            }
            // ✅ Step 2: Detach User from Orders
            List<Orders> orders = ordersRepository.findByUsersId(authorizedUserId);
            if(!orders.isEmpty()){
                for (Orders o : orders){
                    o.setUsers(null);// Nullify the user reference
                    o.setDeletedUserId(-authorizedUserId);// Store deleted user's ID as negative
                    o.setDeliveryAddress(null);//No personal data for deleted user
                    o.setContactPhone(null);//No personal data for deleted user
                    ordersRepository.save(o);
                }
            }
            // ✅ Step 3: Finally, delete the User
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

