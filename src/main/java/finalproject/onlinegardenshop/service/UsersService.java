package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.UsersDto;
import finalproject.onlinegardenshop.entity.Users;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.UsersMapper;
import finalproject.onlinegardenshop.repository.UsersRepository;
import org.apache.catalina.Manager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UsersService {

    private static Logger logger = LogManager.getLogger(UsersService.class);

    private final UsersRepository repository;
    //    private final UsersRepository clientRepository;
    private final UsersMapper mapper;

    @Autowired
    public UsersService(UsersRepository repository, UsersRepository clientRepository, UsersMapper mapper) {
        this.repository = repository;
//        this.clientRepository = clientRepository;
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

}

