package pro.inmost.amazon.chime.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.inmost.amazon.chime.model.entity.User;

import java.util.Optional;

public interface JPAUserRepository extends JpaRepository<User, Long> {

}
