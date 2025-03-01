package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.dto.UsersDto;
import finalproject.onlinegardenshop.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
