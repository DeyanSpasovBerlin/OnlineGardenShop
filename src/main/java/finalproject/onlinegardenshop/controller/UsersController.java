package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.UsersDto;
import finalproject.onlinegardenshop.dto.UsersUpdateDto;
import finalproject.onlinegardenshop.exception.OnlineGardenSchopBadRequestException;
import finalproject.onlinegardenshop.repository.UsersRepository;
import finalproject.onlinegardenshop.service.UsersService;
import jakarta.validation.Valid;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
@Validated
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

    //2.  •	Аутентификация пользователя conrtoller
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UsersDto loginRequestDto) {//? смотри наверх; arg -> email+ pass
        try {
            String token = userService.authenticateUser(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            return ResponseEntity.ok(token); // Return 200 OK if successful
        } catch (OnlineGardenSchopBadRequestException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");//Return 401 Unauthorized
        }
    }
    /*
    in Postman:
    POST /users/login
    {
    "email": "alice@example.com",
    "password": "password123A!"
    } -> 200 OK or -> 401 Unauthorized "Invalid credentials"
     */

    //3. update Users  200 OK, 400 Bad Request, 404 Not Found
    @PutMapping("/{id}")
    public ResponseEntity<?> updatedUsersController(@PathVariable("id") Integer id, @Valid @RequestBody Map<String, Object> requestBody) {
        // проверяем для наличии в query запретные поля check for not allowed field
        if (requestBody.containsKey("password") || requestBody.containsKey("email") || requestBody.containsKey("role")) {
            throw new OnlineGardenSchopBadRequestException("Updating password, email, or role is not allowed.");
        }
        // Convert request body to DTO
        UsersUpdateDto user = new UsersUpdateDto();
        user.setId(id);
        user.setFirstName((String) requestBody.get("firstName"));
        user.setLastName((String) requestBody.get("lastName"));
        user.setPhone((String) requestBody.get("phone"));
        // @Valid work only on dto object. Here we use  Map<String, Object> and firstName,... is not validated with regexp.
        //When manualy add validator we say javac to validate Map
        Set<ConstraintViolation<UsersUpdateDto>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder errorMessages = new StringBuilder();
            for (ConstraintViolation<UsersUpdateDto> violation : violations) {
                errorMessages.append(violation.getMessage()).append(" ");
            }
            throw new OnlineGardenSchopBadRequestException(errorMessages.toString().trim());
        }
        //здесь меняем UsersUpdateDto -> UsersDto потому что в Service
        // возвращаеться UsersDto, потому что mapper работает только с UsersDto
        UsersDto updatedUser = userService.updatedUsersService(user);
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

    // REST API from tex docs:
    //4 •	Удаление учетной записи
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
        return ResponseEntity.accepted().build();
    }
             /*
    corect query in Postman:
    delete: http://localhost:8080/users/{id}
      */

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