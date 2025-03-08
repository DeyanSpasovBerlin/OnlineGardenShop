package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.UsersDto;
import finalproject.onlinegardenshop.entity.Users;
import finalproject.onlinegardenshop.entity.enums.UserRole;
import finalproject.onlinegardenshop.exception.OnlineGardenSchopBadRequestException;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.UsersMapper;
import finalproject.onlinegardenshop.repository.UsersRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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


}

