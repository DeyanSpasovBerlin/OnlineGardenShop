package finalproject.onlinegardenshop.service;

import finalproject.onlinegardenshop.dto.UsersDto;
import finalproject.onlinegardenshop.entity.Cart;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.entity.Users;
import finalproject.onlinegardenshop.exception.OnlineGardenShopBadRequestException;
import finalproject.onlinegardenshop.exception.OnlineGardenShopResourceNotFoundException;
import finalproject.onlinegardenshop.mapper.UsersMapper;
import finalproject.onlinegardenshop.repository.CartItemsRepository;
import finalproject.onlinegardenshop.repository.CartRepository;
import finalproject.onlinegardenshop.repository.OrdersRepository;
import finalproject.onlinegardenshop.repository.UsersRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UsersServiceTest {
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private CartItemsRepository cartItemsRepository;
    @Mock
    private UsersMapper usersMapper;
    @InjectMocks
    private UsersService usersService;
    private List<Users> userList;
    private List<UsersDto> userDtoList;
    private Users user;
    private UsersDto userDto;
    private Cart cart;
    private Orders order;

    @BeforeEach
    void setUp() {
        Users user1 = new Users();
        user1.setId(1);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        Users user2 = new Users();
        user2.setId(2);
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setEmail("jane.doe@example.com");
        userList = List.of(user1, user2);
        UsersDto userDto1 = new UsersDto();
        userDto1.setId(1);
        userDto1.setFirstName("John");
        userDto1.setLastName("Doe");
        userDto1.setEmail("john.doe@example.com");
        UsersDto userDto2 = new UsersDto();
        userDto2.setId(2);
        userDto2.setFirstName("Jane");
        userDto2.setLastName("Doe");
        userDto2.setEmail("jane.doe@example.com");
        userDtoList = List.of(userDto1, userDto2);
        cart = new Cart();
        cart.setId(1);
        cart.setUsers(user1);
        order = new Orders();
        order.setId(1);
        order.setUsers(user1);
        user = user1;
    }

    @Test
    void testGetAllUsersSortedAndPaginated() {
        int page = 0;
        int size = 2;
        String sortField = "firstName";
        String sortDirection = "asc";
        when(usersRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(userList.get(1), userList.get(0)), PageRequest.of(page, size), 2));
        when(usersMapper.entityToDto(userList.get(1))).thenReturn(userDtoList.get(1)); // Jane
        when(usersMapper.entityToDto(userList.get(0))).thenReturn(userDtoList.get(0)); // John
        Page<UsersDto> result = usersService.getAllUsersSortedAndPaginated(page, size, sortField, sortDirection);
        assertEquals(2, result.getTotalElements());
        assertEquals("John", result.getContent().get(1).getFirstName());
        assertEquals("John", result.getContent().get(0).getFirstName());
    }

    @Test
    void testRegisterUser_Success() {
        UsersDto newUserDto = new UsersDto();
        newUserDto.setFirstName("Alice");
        newUserDto.setLastName("Smith");
        newUserDto.setEmail("alice@example.com");
        newUserDto.setPassword("plaintextPassword");
        Users newUserEntity = new Users();
        newUserEntity.setFirstName("Alice");
        newUserEntity.setLastName("Smith");
        newUserEntity.setEmail("alice@example.com");
        newUserEntity.setPassword("encodedPassword");
        Users savedUserEntity = new Users();
        savedUserEntity.setId(10);
        savedUserEntity.setFirstName("Alice");
        savedUserEntity.setLastName("Smith");
        savedUserEntity.setEmail("alice@example.com");
        savedUserEntity.setPassword("encodedPassword");
        UsersDto savedUserDto = new UsersDto();
        savedUserDto.setId(10);
        savedUserDto.setFirstName("Alice");
        savedUserDto.setLastName("Smith");
        savedUserDto.setEmail("alice@example.com");
        when(usersRepository.findByEmail("alice@example.com")).thenReturn(Optional.empty());
        when(usersMapper.dtoToEntity(newUserDto)).thenReturn(newUserEntity);
        when(usersRepository.save(any(Users.class))).thenReturn(savedUserEntity);
        when(usersMapper.entityToDto(savedUserEntity)).thenReturn(savedUserDto);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.encode("plaintextPassword")).thenReturn("encodedPassword");
        usersService = new UsersService(
                usersRepository,
                usersMapper,
                cartRepository,
                ordersRepository,
                null,
                cartItemsRepository,
                encoder
        );
        UsersDto result = usersService.registerUser(newUserDto);
        assertNotNull(result);
        assertEquals("Alice", result.getFirstName());
        assertEquals("alice@example.com", result.getEmail());
        verify(usersRepository, times(1)).save(any(Users.class));
    }
        @Test
        void testRegisterUser_EmailAlreadyExists() {
            UsersDto newUserDto = new UsersDto();
            newUserDto.setEmail("existing@example.com");
            Users existingUser = new Users();
            existingUser.setEmail("existing@example.com");
            when(usersRepository.findByEmail("existing@example.com"))
                    .thenReturn(Optional.of(existingUser));
            OnlineGardenShopBadRequestException exception = assertThrows(
                    OnlineGardenShopBadRequestException.class,
                    () -> usersService.registerUser(newUserDto)
            );
            assertEquals("Email is already in use", exception.getMessage());
            verify(usersRepository, never()).save(any());
        }


    @Test
    void testRegisterUser_EmailFieldIsEmpty() {
        UsersDto invalidUserDto = new UsersDto();
        invalidUserDto.setEmail("");  // Empty email
        invalidUserDto.setPassword("Prrrdmitryyshd_2");  // Valid password to prevent password validation erro
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UsersDto>> violations = validator.validate(invalidUserDto);
        assertFalse(violations.isEmpty());
        Optional<ConstraintViolation<UsersDto>> emailViolation = violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("email"))
                .findFirst();
        assertTrue(emailViolation.isPresent());
        String actualMessage = emailViolation.get().getMessage();
        assertTrue(
                actualMessage.equals("Invalid email format. Email must start with a letter and be in the format example@domain.com")
                        || actualMessage.equals("The email is mandatory!"),
                "Unexpected validation message: " + actualMessage
        );
    }

    @Test
    void testDeleteUser_SuccessfulDeletion() {
        when(usersRepository.findById(1)).thenReturn(Optional.of(user));
        when(cartRepository.findByUsersId(1)).thenReturn(Optional.of(cart));
        when(ordersRepository.findByUsersId(1)).thenReturn(List.of(order));
        usersService.deleteUserByAdmin(1);
        verify(cartItemsRepository, times(1)).deleteByCartId(cart.getId());
        verify(cartRepository, times(1)).delete(cart);
        verify(ordersRepository, times(1)).save(any(Orders.class));
        verify(usersRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(usersRepository.findById(1)).thenReturn(Optional.empty());
        OnlineGardenShopResourceNotFoundException exception = assertThrows(
                OnlineGardenShopResourceNotFoundException.class,
                () -> usersService.deleteUserByAdmin(1)
        );
        assertEquals("User with id = 1 not found in database!", exception.getMessage());
        verify(cartItemsRepository, never()).deleteByCartId(anyInt());
        verify(cartRepository, never()).delete(any());
        verify(ordersRepository, never()).save(any());
        verify(usersRepository, never()).deleteById(anyInt());
    }
}

