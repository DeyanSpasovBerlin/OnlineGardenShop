package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.UsersDto;
import finalproject.onlinegardenshop.dto.UsersUpdateDto;
import finalproject.onlinegardenshop.entity.Users;
import finalproject.onlinegardenshop.entity.enums.UserRole;
import finalproject.onlinegardenshop.exception.OnlineGardenSchopBadRequestException;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.UsersMapper;
import finalproject.onlinegardenshop.repository.UsersRepository;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UsersService {

    private static Logger logger = LogManager.getLogger(UsersService.class);

    private final UsersRepository repository;
    private final UsersMapper mapper;

    @Autowired
    public UsersService(UsersRepository repository,  UsersMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<UsersDto> getAll(){
        List<Users> users = repository.findAll();
        logger.debug("Users retrieved from db");
        logger.debug("user ids: {}", () -> users.stream().map(Users::getId).toList());
        return mapper.entityListToDto(users);
    }

    public UsersDto getUsersById(Integer id) {
        Optional<Users> optional = repository.findById(id);
        if (optional.isPresent()) {
            UsersDto found = mapper.entityToDto(optional.get()) ;
            return found;
        }
        throw new OnlineGardenShopResourceNotFoundException("User with id = " + id + " not found in database");
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
    //    1 •	Регистрация пользователя ->   service

    @Transactional
    public UsersDto registerUser(UsersDto usersDto) {
        logger.debug("Registering new user with email: {}", usersDto.getEmail());
        // Check if email already exists
        Optional<Users> existingUser = repository.findByEmail(usersDto.getEmail());
        if (existingUser.isPresent()) {
            throw new OnlineGardenSchopBadRequestException("Email is already in use");//400 Bad Request
            // or if Invalid input (missing fields, wrong format) -> "email": "Email is required"
        }
        // Convert DTO to entity and save to DB
        Users user = mapper.dtoToEntity(usersDto);
        user.setRole(UserRole.CLIENT); // Default role
        Users savedUser = repository.save(user);
        logger.info("User registered successfully: {}", savedUser.getEmail());//201 Created
        return mapper.entityToDto(savedUser);
    }

    //2.  • Аутентификация пользователя Service
    public String authenticateUser(String email, String password) {
        Users user = repository.findByEmail(email)
                .orElseThrow(() -> new OnlineGardenSchopBadRequestException("Invalid credentials"));// Find the user by email
        if (!password.equals(user.getPassword())) {// Compare input password with stored password (plain-text comparison)
            throw new OnlineGardenSchopBadRequestException("Invalid credentials");
        }
        System.out.println("User " + email + " successfully authenticated.");// Log successful authentication
        return "User authenticated successfully!";// Return success message after authentication
    }

    //3. update Users  200 OK, 400 Bad Request, 404 Not Found
    @Transactional
    public UsersDto updatedUsersService(@Valid UsersUpdateDto usersDto) {
        Optional<Users> optional = repository.findById(usersDto.getId());
        if (optional.isPresent()) {
            Users existingUser = optional.get();
// если какаое-то поле не задано, задаем его вручную от старого ентити manualy set existing fields
            if (usersDto.getFirstName() != null) existingUser.setFirstName(usersDto.getFirstName());
            if (usersDto.getLastName() != null) existingUser.setLastName(usersDto.getLastName());
            if (usersDto.getPhone() != null) existingUser.setPhone(usersDto.getPhone());
            Users savedUser = repository.save(existingUser);
            return mapper.entityToDto(savedUser);
        }
        throw new OnlineGardenShopResourceNotFoundException("User with id = " + usersDto.getId() + " not found in database");
    }

    // REST API from tex docs:
    //4 •	Удаление учетной записи
//    public void deleteUser(Integer id){repository.deleteById(id);} ето не работает- спросить почему!?
 @Transactional
    public void deleteUser(Integer id){//ето работает- почему - в чем разница?
        Optional<Users> userForDelete = repository.findById(id);
        if (userForDelete.isPresent()) {;
            repository.deleteById(id);
        } else {
            throw new OnlineGardenShopResourceNotFoundException("User with id = " + id + " not found in database");
        }
    }

}
/*
    all query for testing Users:
    http://localhost:8080/users/all
    http://localhost:8080/users/9
    POST http://localhost:8080/users/register
        {
            "lastName": "LastFinalProbe",
            "firstName": "FirstFinalProbe",
            "email": "FirstFinalProbe@example.com",
            "phone": "909876543218",
            "password": "FirstFinalProbeSecure_1"
    }
    POST http://localhost:8080/users/login
        {
            "email": "FirstFinalProbe@example.com",
            "password": "FirstFinalProbeSecure_1"
    }
    DELETE http://localhost:8080/users/17
 */
