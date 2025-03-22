package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import finalproject.onlinegardenshop.entity.enums.UserRole;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsersRepositoryTest {

    @Autowired
    private UsersRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void findByFirstLetterFromFirstNameAndFirstLetterFromLastName() {
        // Arrange
        repository.deleteAll();  // Ensure test isolation
        testEntityManager.clear();

        Users user1 = new Users();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        testEntityManager.persist(user1);

        Users user2 = new Users();
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        testEntityManager.persist(user2);

        testEntityManager.flush();

        // Act
        List<Users> result = repository.findByFirstLetterFromFirstNameAndFirstLetterFromLastName("J", "D");

        // Debugging: Print all results
        System.out.println("Found users: " + result);

        // Assert - Ensure only test users are counted
        long testUsersCount = result.stream()
                .filter(u -> u.getLastName().equals("Doe"))
                .count();

        assertEquals(2, testUsersCount); // Expect only John & Jane Doe
    }

    @Test
    void updateStatus(){
        // Arrange: Create and persist a test user
        Users user = new Users();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setRole(UserRole.CLIENT); // Assuming default role
        testEntityManager.persist(user);
        testEntityManager.flush();

        // Act: Update the user's role
        int updatedRows = repository.updateStatus(Long.valueOf(user.getId()), UserRole.ADMIN);
        testEntityManager.clear(); // Clear persistence context to fetch fresh data

        // Assert: Fetch the updated user and verify the change
        Users updatedUser = testEntityManager.find(Users.class, user.getId());
        assertEquals(1, updatedRows, "Update should affect 1 row");
        assertNotNull(updatedUser, "Updated user should not be null");
        assertEquals(UserRole.ADMIN, updatedUser.getRole(), "User role should be updated to ADMIN");

    }
}