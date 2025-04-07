package finalproject.onlinegardenshop.encoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CheckEncodedPasswords {

    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("Password_1"));  //
        // Password_1:       $2a$10$AyN5YKOZEr5r/5ju5WyinuIQMFCqZculR/mnGdaBngNUNrVy6k6ce
        //TestPassword_1:    $2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji
    }


}
