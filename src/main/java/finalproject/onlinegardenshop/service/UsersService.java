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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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




    public List<UsersDto> getAll(){
        List<Users> users = repository.findAll();
        logger.debug("Users retrieved from db");
        logger.debug("user ids: {}", () -> users.stream().map(Users::getId).toList());
        return mapper.entityListToDto(users);
    }

//    public UsersDto getUsersById(Integer id) {
//        Optional<Users> optional = repository.findById(id);
//        if (optional.isPresent()) {
//            UsersDto found = mapper.entityToDto(optional.get()) ;
//            return found;
//        }
//        throw new OnlineGardenShopResourceNotFoundException("User with id = " + id + " not found in database");
//    }

    public UsersDto getUsersById(Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new AccessDeniedException("Unauthorized access");
        }
        String email = (String) auth.getPrincipal(); //  Извличаме e-mail от токена
        logger.info("Authenticated user email: {}", email);
        Optional<Users> optional = repository.findByEmail(email);
        if (optional.isEmpty()) {
            logger.error("No user found with email: {}", email);
            throw new OnlineGardenShopBadRequestException("User not found");
        }
        Users user = optional.get();
        // Ако ролята е CLIENT, проверяваме дали иска собственото си ID
        if (user.getRole() == UserRole.CLIENT && !user.getId().equals(id)) {
            throw new AccessDeniedException("Clients can only access their own data.");
        }
        return mapper.entityToDto(repository.findById(id)
                .orElseThrow(() -> new OnlineGardenShopBadRequestException("User not found")));
    }

    public List<UsersDto> findByName(String firstName){
        List<Users> users = repository.findByFirstName(firstName);
        return mapper.entityListToDto(users);
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

    //2.  • Аутентификация пользователя Service  ето вариант до Spring Security
//    public String authenticateUser(String email, String password) {
//        Users user = repository.findByEmail(email)
//                .orElseThrow(() -> new OnlineGardenShopBadRequestException("Invalid credentials"));// Find the user by email
//        if (!password.equals(user.getPassword())) {// Compare input password with stored password (plain-text comparison)
//            throw new OnlineGardenShopBadRequestException("Invalid credentials");
//        }
//        System.out.println("User " + email + " successfully authenticated.");// Log successful authentication
//        return "User authenticated successfully!";// Return success message after authentication
//    }

    //3. update Users  200 OK, 400 Bad Request, 404 Not Found
//    @Transactional
//    public UsersDto updatedUsersService(@Valid UsersUpdateDto usersDto) {//UsersUpdateDto have only this field,
//        // which is alloyed to change. This prevent from changing E-mail, ID and Pass.
//        //UsersDto has all field. In return we return full Dto with field changed only by fied in restricted UsersUpdateDto
//        Optional<Users> optional = repository.findById(usersDto.getId());
//        if (optional.isPresent()) {
//            Users existingUser = optional.get();
//// если какаое-то поле не задано, задаем его вручную от старого ентити manualy set existing fields
//            if (usersDto.getFirstName() != null) existingUser.setFirstName(usersDto.getFirstName());
//            if (usersDto.getLastName() != null) existingUser.setLastName(usersDto.getLastName());
//            if (usersDto.getPhone() != null) existingUser.setPhone(usersDto.getPhone());
//            Users savedUser = repository.save(existingUser);
//            return mapper.entityToDto(savedUser);
//        }
//        throw new OnlineGardenShopResourceNotFoundException("User with id = " + usersDto.getId() + " not found in database");
//    }

//******************************************

    @Transactional
    public UsersDto updatedUsersService(Integer id, @Valid UsersUpdateDto usersDto) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
//            throw new AccessDeniedException("Unauthorized access");
//        }
//        String email = (String) auth.getPrincipal(); //  Извличаме e-mail от токена
//        logger.info("Authenticated user email: {}", email);
//        Optional<Users> optional = repository.findByEmail(email);
//        if (optional.isEmpty()) {
//            logger.error("No user found with email: {}", email);
//            throw new OnlineGardenShopBadRequestException("User not found");
//        }
//        Users existingUser = optional.get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users existingUser = repository.findByEmail((String) auth.getPrincipal()).get();//find user, who is authorized
        // Ако ролята е CLIENT, проверяваме дали иска собственото си ID
        if (existingUser.getRole() == UserRole.CLIENT && !existingUser.getId().equals(id)) {
            throw new AccessDeniedException("Clients can only access their own data.");
        }
        Optional<Users> optionalId = repository.findById(usersDto.getId());
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

//********************************************************************************


    // REST API from tex docs:
    //4 •	Удаление учетной записи
        @Transactional
    public void deleteUser(Integer userId){
        //✅ Общая идея: Users<-oneToOne<-Cart->oneToMany->CartItems;
        // Начинаем в обратном порядки: впервые del CartItemsр потом Cart и наконец Users
        //Users <- onetoMany<-Orders: впервые во всеф Orders которые делал етот User всавим userId=null.
        //Не del Orders  как вверх del Cart and CartItems, потому что мы хотим что бы все Orders хранилис,
        // даже если User del для статистики и судебных разбирательств. А Cart and CartItems привязанные к User
        // и их можно удалить вместе с ним. Здесь принцип таков- идем в обратную сторону по цепи relations преди да
        //стигнем до User  and del
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users authorizedUser = repository.findByEmail((String) auth.getPrincipal()).get();//find user, who is authorized
        // Ако ролята е CLIENT, проверяваме дали иска собственото си ID
        if (authorizedUser.getRole() == UserRole.CLIENT && !authorizedUser.getId().equals(userId)) {
            throw new AccessDeniedException("Clients can only access their own data.");
        }
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
            throw new OnlineGardenShopResourceNotFoundException("Manager with id = " + userId + " not found in database!");
        }
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

