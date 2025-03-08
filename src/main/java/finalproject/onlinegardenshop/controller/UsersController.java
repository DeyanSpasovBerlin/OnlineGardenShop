package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.UsersDto;
import finalproject.onlinegardenshop.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Validated
public class UsersController {
    private UsersService userService;

    @Autowired
    public UsersController(UsersService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
        public List<UsersDto> getAllUserrs() {
        return userService.getAll();
    }

    @GetMapping("{id}")
    public Optional<UsersDto> getUsersById(@PathVariable Integer id) {
        return Optional.ofNullable(userService.getUsersById(id));
    }

    @GetMapping("/search")
    public List<UsersDto> findByFirstName(@RequestParam String firstName){
        return userService.findByName(firstName);
    }

    @GetMapping("/searchByFirstAndLastName")
    public List<UsersDto> findByFirstNameAndLastName(@RequestParam String firstName, @RequestParam String lastName){
        return userService.findByFirstNameAndLastName(firstName,lastName);
    }

    @GetMapping("/searchByFirstLetterFromFirstNameAndFirstLetterFromLastName")
    public List<UsersDto> findFirstLetterFromFirstNameAndFirstLetterFromLastName(
            @RequestParam String firstName, @RequestParam String lastName){
        return userService.findFirstLetterFromFirstNameAndFirstLetterFromLastName(firstName,lastName);
    }

    // REST API from tex docs:
    //    1 •	Регистрация пользователя  ->controller

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UsersDto usersDto) {// здесь ? означает, что можно вернуть
        //UsersDto, ErrorResponse, null. Таким оброзом не надо изпользоват if
        UsersDto createdUser = userService.registerUser(usersDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    /*
    Postman:
    post -> http://localhost:8080/users/register
            {
              "firstName": "John",
              "lastName": "Doe",
              "email": "john.doe@example.com",
              "phone": "1234567890",
              "password": "securePass123"
            }  -> 201 Created
            {
              "firstName": "John",
              "lastName": "Doe",
              "phone": "1234567890",
              "password": "securePass123"
            } -> "email": "Email is required"  400 Bad Request
     */


}
