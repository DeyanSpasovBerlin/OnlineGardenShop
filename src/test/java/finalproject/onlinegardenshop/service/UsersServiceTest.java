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
        // Assign user1 to user to avoid NullPointerException in testDeleteUser_SuccessfulDeletion()
        user = user1;
    }

    @Test
    void getAll_ShouldReturnAllUsers() {
        when(usersRepository.findAll()).thenReturn(userList);
        when(usersMapper.entityListToDto(userList)).thenReturn(userDtoList);
        List<UsersDto> result = usersService.getAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(usersRepository, times(1)).findAll();
        verify(usersMapper, times(1)).entityListToDto(userList);
    }


//    @Test
//    void testGetUsersById_Found() {
//        // Arrange
//        when(usersRepository.findById(1)).thenReturn(Optional.of(userList.get(0)));
//        when(usersMapper.entityToDto(userList.get(0))).thenReturn(userDtoList.get(0));
//        // Act
//        UsersDto result = usersService.getUsersById(1);
//        // Assert
//        assertNotNull(result);
//        assertEquals(userDtoList.get(0), result);
//        verify(usersRepository, times(1)).findById(1);
//        verify(usersMapper, times(1)).entityToDto(userList.get(0));
//    }

//    @Test
//    void testGetUsersById_NotFound() {
//        // Arrange
//        when(usersRepository.findById(3)).thenReturn(Optional.empty());
//        // Act & Assert
//        OnlineGardenShopResourceNotFoundException exception = assertThrows(OnlineGardenShopResourceNotFoundException.class, () -> {
//            usersService.getUsersById(3);
//        });
//        assertEquals("User with id = 3 not found in database", exception.getMessage());
//        verify(usersRepository, times(1)).findById(3);
//    }

    @Test
    void testRegisterUser_EmailAlreadyInUse() {
        // Arrange
        when(usersRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(userList.get(0)));
        // Act & Assert
        OnlineGardenShopBadRequestException exception = assertThrows(OnlineGardenShopBadRequestException.class, () -> {
            usersService.registerUser(userDtoList.get(0));
        });
        assertEquals("Email is already in use", exception.getMessage());
        verify(usersRepository, times(1)).findByEmail("john.doe@example.com");
        verify(usersMapper, times(0)).dtoToEntity(any());
    }

    @Test
    void testRegisterUser_SuccessfulRegistration() {
        // Arrange
        when(usersRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());
        when(usersMapper.dtoToEntity(userDtoList.get(0))).thenReturn(userList.get(0));
        when(usersRepository.save(userList.get(0))).thenReturn(userList.get(0));
        when(usersMapper.entityToDto(userList.get(0))).thenReturn(userDtoList.get(0));
        // Act
        UsersDto result = usersService.registerUser(userDtoList.get(0));
        // Assert
        assertNotNull(result);
        assertEquals(userDtoList.get(0), result);
        verify(usersRepository, times(1)).findByEmail("john.doe@example.com");
        verify(usersMapper, times(1)).dtoToEntity(userDtoList.get(0));
        verify(usersRepository, times(1)).save(userList.get(0));
        verify(usersMapper, times(1)).entityToDto(userList.get(0));
    }

    @Test
    void testRegisterUser_EmailFieldIsEmpty() {
        // Arrange
        UsersDto invalidUserDto = new UsersDto();
        invalidUserDto.setEmail("");  // Empty email
        invalidUserDto.setPassword("Prrrdmitryyshd_2");  // Valid password to prevent password validation erro
        // Act
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UsersDto>> violations = validator.validate(invalidUserDto);
        // Assert
        assertFalse(violations.isEmpty());
        // Find the specific email validation message
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
        usersService.deleteUser(1);
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
                () -> usersService.deleteUser(1)
        );
        assertEquals("Manager with id = 1 not found in database!", exception.getMessage());
        verify(cartItemsRepository, never()).deleteByCartId(anyInt());
        verify(cartRepository, never()).delete(any());
        verify(ordersRepository, never()).save(any());
        verify(usersRepository, never()).deleteById(anyInt());
    }
}

