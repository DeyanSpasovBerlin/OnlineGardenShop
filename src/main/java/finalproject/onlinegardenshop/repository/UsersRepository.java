package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Users;
import finalproject.onlinegardenshop.entity.enums.UserRole;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users,Integer> {
    List<Users> findByFirstName(String name);

    List<Users> findByFirstNameAndLastName(String firstName,String lastname);

    @Query("select u from Users u where u.lastName like :lastname% and u.firstName like :firstName%")
    List<Users> findByFirstLetterFromFirstNameAndFirstLetterFromLastName(String firstName, String lastname);

    @Query("update Users u set u.role = :status where u.id = :id")
    @Modifying
    int updateStatus(Long id, UserRole status);

}
