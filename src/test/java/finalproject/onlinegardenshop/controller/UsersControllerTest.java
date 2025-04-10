package finalproject.onlinegardenshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalproject.onlinegardenshop.config.SecurityConfig;
import finalproject.onlinegardenshop.entity.enums.UserRole;
import finalproject.onlinegardenshop.repository.UsersRepository;
import finalproject.onlinegardenshop.security.JwtProvider;
import finalproject.onlinegardenshop.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UsersController.class)
@Import(SecurityConfig.class)
class UsersControllerTest {
    @MockitoBean
    private UsersService service;
    @MockitoBean
    private JwtProvider jwtProvider;
    @MockitoBean
    private UsersRepository repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

        @Test
        @WithMockUser(roles = "ADMIN")
        void testGetAllUsersSortedPaginated_ShouldReturnUsersPage() throws Exception {
            UsersDto user1 = new UsersDto();
            user1.setId(1);
            user1.setFirstName("Alice");
            user1.setLastName("Smith");
            user1.setEmail("alice@example.com");
            UsersDto user2 = new UsersDto();
            user2.setId(2);
            user2.setFirstName("Bob");
            user2.setLastName("Brown");
            user2.setEmail("bob@example.com");
            List<UsersDto> users = List.of(user1, user2);
            Page<UsersDto> userPage = new org.springframework.data.domain.PageImpl<>(users);
            when(service.getAllUsersSortedAndPaginated(anyInt(), anyInt(), anyString(), anyString()))
                    .thenReturn(userPage);
            mockMvc.perform(get("/users/sorted")
                            .param("page", "0")
                            .param("size", "2")
                            .param("sortField", "firstName")
                            .param("sortDirection", "asc")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].firstName", is("Alice")))
                    .andExpect(jsonPath("$.content[1].firstName", is("Bob")))
                    .andExpect(jsonPath("$.totalElements", is(2)));
            verify(service, times(1))
                    .getAllUsersSortedAndPaginated(0, 2, "firstName", "asc");
        }

        @Test
        @WithMockUser(roles = "CLIENT")
        void testGetAllUsersSortedPaginated_ForbiddenForNonAdmin() throws Exception {
            mockMvc.perform(get("/users/sorted")
                            .param("page", "0")
                            .param("size", "2")
                            .param("sortField", "firstName")
                            .param("sortDirection", "asc"))
                    .andExpect(status().isForbidden());

            verify(service, never()).getAllUsersSortedAndPaginated(anyInt(), anyInt(), anyString(), anyString());
        }

        @Test
       @WithMockUser(username = "Test user", roles = {"ADMIN"})
        void  registerUser() throws Exception {
            UsersDto usersDto = new UsersDto(
                null,
                "Doe",
                "John",
                "john.doe@example.com",
                "+1234567890",
                "SecurePassword123!",
                UserRole.CLIENT
        );
        UsersDto createdUserDto = new UsersDto(
                1,
                "Doe",
                "John",
                "john.doe@example.com",
                "+1234567890",
                "SecurePassword123!",
                UserRole.CLIENT
        );
        when(service.registerUser(any(UsersDto.class))).thenAnswer(invocation -> {
            UsersDto capturedUserDto = invocation.getArgument(0);
            capturedUserDto.setId(1);
            return capturedUserDto;
        });
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usersDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.id").value(1)); // Dynamically assigned id
    }

    @Test
    @WithMockUser(username = "Test user", roles = {"ADMIN"})
    void deleteUser() throws Exception {
        Long userId = 1L;
        doNothing().when(service).deleteUserByAdmin(Math.toIntExact(userId));
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isAccepted());
        verify(service, times(1)).deleteUserByAdmin(Math.toIntExact(userId));

    }
}

