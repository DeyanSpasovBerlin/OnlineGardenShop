package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.UsersDto;
import finalproject.onlinegardenshop.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Validated
public class UsersControler {

    private UsersService userService;

    @Autowired
    public UsersControler(UsersService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<UsersDto> getAllManagers() {
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
}
