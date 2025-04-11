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
                null,
                "Doe",
                "John",
                "john.doe@example.com",
                "+1234567890",
                "SecurePassword123!",
                UserRole.CLIENT
        );
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usersDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.id").isNotEmpty());
        var createdUser = repository.findByEmail("john.doe@example.com").orElseThrow();
        Long userId = Long.valueOf(createdUser.getId());
        assert repository.existsById(userId.intValue()) : "User should exist after registration";
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isAccepted());
        boolean userStillExists = repository.findById(userId.intValue()).isPresent();
        assert !userStillExists : "User should be deleted";

    }
}
