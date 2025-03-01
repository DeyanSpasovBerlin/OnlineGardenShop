package finalproject.onlinegardenshop.repository;

import finalproject.onlinegardenshop.entity.Users;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users,Integer> {
    List<Users> findByName(String name);
}
