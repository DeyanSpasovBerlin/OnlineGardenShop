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
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private final Validator validator;
    private final UsersRepository usersRepository;

    @Autowired
    public UsersController(UsersService userService, Validator validator, UsersRepository usersRepository) {
        this.userService = userService;
        this.validator = validator;
        this.usersRepository = usersRepository;
    }

    @Operation(summary = "Returns sorted and paginated list of users for use from ADMIN")
    @GetMapping("/sorted")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UsersDto>> getAllUsersSortedPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        return ResponseEntity.ok(userService.getAllUsersSortedAndPaginated(page, size, sortField, sortDirection));
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
    @Operation(summary = "Returns a list of users based on the specified first name for use from ADMIN")
    public List<UsersDto> findByFirstName(@RequestParam String firstName, @RequestParam String lastName){
        return userService.findByFirstNameAndLastName(firstName,lastName);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/searchByFirstAndLastName")
    @Operation(summary = "Returns a list of users based on the specified first and last name for use from ADMIN")
    public List<UsersDto> findByFirstNameAndLastName(@RequestParam String firstName, @RequestParam String lastName){
        return userService.findByFirstNameAndLastName(firstName,lastName);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/searchByFirstLetterFromFirstNameAndFirstLetterFromLastName")
    @Operation(summary = "Returns a list of users based on the specified first " +
            "letter of the first name and first letter of the last name for use from ADMIN")
    public List<UsersDto> findFirstLetterFromFirstNameAndFirstLetterFromLastName(
            @RequestParam String firstName, @RequestParam String lastName){
        return userService.findFirstLetterFromFirstNameAndFirstLetterFromLastName(firstName,lastName);
    }

    @PostMapping("/register")
    @Operation(summary = "Creates a new user in the app,available without login")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UsersDto usersDto) {
        UsersDto createdUser = userService.registerUser(usersDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Introduces desired changes to the data of the user selected by id for emergency use from ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> updatedUsersByAdmin(@PathVariable("id") Integer id, @Valid @RequestBody UsersUpdateDto usersUpdateDto) {
        UsersDto updatedUser = userService.updatedUsersByAdmin(id, usersUpdateDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.ACCEPTED);
    }

         @PutMapping()
         @Operation(summary = "Introduces desired changes to the data of the user who is authorized")
         public ResponseEntity<?> updatedUsersByUser( @Valid @RequestBody UsersUpdateDto usersUpdateDto) {
             UsersDto updatedUser = userService.updatedUsersByUser(usersUpdateDto);
             return new ResponseEntity<>(updatedUser, HttpStatus.ACCEPTED);
         }

    @DeleteMapping("{id}")
    @Operation(summary = "Deletes a certain user selected by id for emergency use from ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUserByAdmin(@PathVariable Integer id){
        userService.deleteUserByAdmin(id);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping()
    @Operation(summary = "Deletes a certain user by himself, when is authorized")
    public ResponseEntity<Void> deleteUserByUser(){
        userService.deleteUserByUser();
        return ResponseEntity.accepted().build();
    }

}
