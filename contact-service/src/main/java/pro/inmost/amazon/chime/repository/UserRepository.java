package pro.inmost.amazon.chime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.inmost.amazon.chime.model.dto.ContactDetail;
import pro.inmost.amazon.chime.model.entity.Contacts;
import pro.inmost.amazon.chime.model.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    @Query(value = "SELECT u FROM User u WHERE u.username LIKE %:username%")
    List<User> findUsersByUsername(String username);
}
