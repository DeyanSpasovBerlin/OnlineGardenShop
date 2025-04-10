package finalproject.onlinegardenshop;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalproject.onlinegardenshop.dto.UsersDto;
import finalproject.onlinegardenshop.entity.enums.UserRole;
import finalproject.onlinegardenshop.repository.UsersRepository;
import finalproject.onlinegardenshop.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsersRepository repository;

    @Autowired
    private UsersService service;

    @Test
    void contextLoads() {
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void registerAndDeleteUser() throws Exception {
        UsersDto usersDto = new UsersDto(
                null, // id is null here
                "Doe",
                "John",
                "john.doe@example.com",
                "+1234567890",
                "SecurePassword123!",
                UserRole.CLIENT
        );

        // Perform a POST request to the /users/register endpoint
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usersDto)))
                // Verify that the response status is 201 (Created)
                .andExpect(status().isCreated())
                // Verify that the response body contains the registered user's email
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                // Verify that the id field is returned in the response
                .andExpect(jsonPath("$.id").isNotEmpty()); // Any valid ID should be returned

        // Fetch the created user from the DB to get its ID
        var createdUser = repository.findByEmail("john.doe@example.com").orElseThrow();
        Long userId = Long.valueOf(createdUser.getId());

        // Perform DELETE request
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isAccepted());

    }
}
