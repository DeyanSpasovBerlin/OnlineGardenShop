package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.UsersDto;
import finalproject.onlinegardenshop.dto.UsersUpdateDto;
import finalproject.onlinegardenshop.exception.OnlineGardenShopBadRequestException;
import finalproject.onlinegardenshop.repository.UsersRepository;
import finalproject.onlinegardenshop.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
@Validated
@Tag(name = "Users Controller", description = "REST API for managing users in the app")
public class UsersController {
    private final UsersService userService;
    private final Validator validator;//this is need in updatedUsersController
    private final UsersRepository usersRepository;

    @Autowired
    public UsersController(UsersService userService, Validator validator, UsersRepository usersRepository) {
        this.userService = userService;
        this.validator = validator;
        this.usersRepository = usersRepository;
    }

    @Operation(summary = "Returns a list of all app users")
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
        public List<UsersDto> getAllUserrs() {
        return userService.getAll();
    }

    @GetMapping("{id}")
    @Operation(summary = "Returns a user by id for ADMIN purpose")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<UsersDto> getUsersByIdForAdmin(@PathVariable Integer id) {
        UsersDto userDto = userService.getUsersByIdForAdmin(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/user")
    @Operation(summary = "Returns a user who is authorized")
    public ResponseEntity<UsersDto> getUsersById() {
        UsersDto userDto = userService.getUsersById();
        return ResponseEntity.ok(userDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/search")
    @Operation(summary = "Returns a list of users based on the specified first name")
    public List<UsersDto> findByFirstName(@RequestParam String firstName, @RequestParam String lastName){
        return userService.findByFirstNameAndLastName(firstName,lastName);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/searchByFirstAndLastName")
    @Operation(summary = "Returns a list of users based on the specified first and last name")
    public List<UsersDto> findByFirstNameAndLastName(@RequestParam String firstName, @RequestParam String lastName){
        return userService.findByFirstNameAndLastName(firstName,lastName);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/searchByFirstLetterFromFirstNameAndFirstLetterFromLastName")
    @Operation(summary = "Returns a list of users based on the specified first letter of the first name and first letter of the last name")
    public List<UsersDto> findFirstLetterFromFirstNameAndFirstLetterFromLastName(
            @RequestParam String firstName, @RequestParam String lastName){
        return userService.findFirstLetterFromFirstNameAndFirstLetterFromLastName(firstName,lastName);
    }

    // REST API from tex docs:
    //    1 •	Регистрация пользователя  ->controller

    @PostMapping("/register")
    @Operation(summary = "Creates a new user in the app")
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
              "phone": "+48 101 1234567",
              "password": "securePass_123"
            }  -> 201 Created
            {
              "firstName": "John",
              "lastName": "Doe",
              "phone": "1234567890",
              "password": "securePass123"
            } -> "email": "Email is required"  400 Bad Request
     */

    //3. update Users  200 OK, 400 Bad Request, 404 Not Found
    @PutMapping("/{id}")
    @Operation(summary = "Introduces desired changes to the data of the user selected by id")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> updatedUsersByAdmin(@PathVariable("id") Integer id, @Valid @RequestBody UsersUpdateDto usersUpdateDto) {
//        //use UsersUpdateDto becouse only FirstName,LastName and Phone is alloyed to change
//        UsersUpdateDto newUser = new UsersUpdateDto();
//        newUser.setId(id);
//        newUser.setFirstName((String) user.getFirstName());
//        newUser.setLastName((String) user.getLastName());
//        newUser.setPhone((String) user.getPhone());
//        Set<ConstraintViolation<UsersUpdateDto>> violations = validator.validate(user);
//        if (!violations.isEmpty()) {
//            StringBuilder errorMessages = new StringBuilder();
//            for (ConstraintViolation<UsersUpdateDto> violation : violations) {
//                errorMessages.append(violation.getMessage()).append(" ");
//            }
//            throw new OnlineGardenShopBadRequestException(errorMessages.toString().trim());
//        }
        //здесь меняем UsersUpdateDto -> UsersDto потому что в Service
        // возвращаеться UsersDto, потому что mapper работает только с UsersDto
        UsersDto updatedUser = userService.updatedUsersByAdmin(id, usersUpdateDto);//
        return new ResponseEntity<>(updatedUser, HttpStatus.ACCEPTED);
    }
         /*
    corect query in Postman:
    put: http://localhost:8080/users/{id}
    {
    "lastName": "string",
    "firstName": "string",
    "phone": "string"
      */
    //***********************************
         @PutMapping()
         @Operation(summary = "Introduces desired changes to the data of the user who is authorized")
         public ResponseEntity<?> updatedUsersByUser( @Valid @RequestBody UsersUpdateDto usersUpdateDto) {
             UsersDto updatedUser = userService.updatedUsersByUser(usersUpdateDto);//
             return new ResponseEntity<>(updatedUser, HttpStatus.ACCEPTED);
         }

    //*********************************

    // REST API from tex docs:
    //4 •	Удаление учетной записи
    @DeleteMapping("{id}")
    @Operation(summary = "Deletes a certain user selected by id from ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUserByAdmin(@PathVariable Integer id){
        userService.deleteUserByAdmin(id);
        return ResponseEntity.accepted().build();
    }
             /*
    corect query in Postman:
    delete: http://localhost:8080/users/{id}
      */

    @DeleteMapping()
    @Operation(summary = "Deletes a certain user by himself")
    public ResponseEntity<Void> deleteUserByUser(){
        userService.deleteUserByUser();
        return ResponseEntity.accepted().build();
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
/*
{
        "id": 2,
        "lastName": "Smith",
        "firstName": "Bob",
        "email": "bob@example.com",
        "phone": "2345678901",
        "password": "securePass1",
        "role": "CLIENT"
    },
 */