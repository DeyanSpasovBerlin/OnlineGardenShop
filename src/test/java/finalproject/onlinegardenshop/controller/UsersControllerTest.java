package finalproject.onlinegardenshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalproject.onlinegardenshop.entity.enums.UserRole;
import finalproject.onlinegardenshop.repository.UsersRepository;
import finalproject.onlinegardenshop.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import finalproject.onlinegardenshop.dto.UsersDto;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UsersController.class)
class UsersControllerTest {

    @MockitoBean
    private UsersService service;

    @MockitoBean
    private UsersRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllUserrs() throws Exception{
        // Arrange: Create mock UsersDto list
        UsersDto user1 = new UsersDto(
                1,
                "Doe",
                "John",
                "john.doe@example.com",
                "+49123456789",
                "SecurePass1!",
                UserRole.CLIENT
        );
        UsersDto user2 = new UsersDto(
                2,
                "Doe",
                "Jane",
                "jane.doe@example.com",
                "+49123456788",
                "AnotherPass1!",
                UserRole.ADMIN
        );
        List<UsersDto> usersList = List.of(user1, user2);

        // Mock service call
        when(service.getAll()).thenReturn(usersList);

        // Act & Assert
        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()", is(2))) // Check list size
                .andExpect(jsonPath("$[0].id", is(1))) // Verify first user
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[0].email", is("john.doe@example.com")))
                .andExpect(jsonPath("$[1].id", is(2))) // Verify second user
                .andExpect(jsonPath("$[1].firstName", is("Jane")))
                .andExpect(jsonPath("$[1].lastName", is("Doe")))
                .andExpect(jsonPath("$[1].email", is("jane.doe@example.com")));
    }

    @Test
    void  registerUser() throws Exception {

        UsersDto usersDto = new UsersDto(
                null, // id is null here
                "Doe",
                "John",
                "john.doe@example.com",
                "+1234567890",
                "SecurePassword123!",
                UserRole.CLIENT
        );

        // Mock the service to return a UsersDto with an id (simulating DB behavior)
        // Simulate the response having an ID assigned dynamically
        UsersDto createdUserDto = new UsersDto(
                1, // Mocked id assignment (it can be any number, depending on your test scenario)
                "Doe",
                "John",
                "john.doe@example.com",
                "+1234567890",
                "SecurePassword123!",
                UserRole.CLIENT
        );

        // Capture the UsersDto passed to the service and return the one with the id
        when(service.registerUser(any(UsersDto.class))).thenAnswer(invocation -> {
            UsersDto capturedUserDto = invocation.getArgument(0);
            // Assign an id to the captured user, simulating a DB save
            capturedUserDto.setId(1); // Set the dynamic ID you want
            return capturedUserDto;
        });

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
                .andExpect(jsonPath("$.id").value(1)); // Dynamically assigned id
    }

    @Test
    void deleteUser() throws Exception {
        // Given
        Long userId = 1L;

        // Mock service behavior
        doNothing().when(service).deleteUser(Math.toIntExact(userId));

        // When & Then
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isAccepted());

        // Verify that the service method was called
        verify(service, times(1)).deleteUser(Math.toIntExact(userId));

    }
}

