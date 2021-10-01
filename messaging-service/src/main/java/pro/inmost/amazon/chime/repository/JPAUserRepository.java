package pro.inmost.amazon.chime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.inmost.amazon.chime.model.entity.User;

public interface JPAUserRepository extends JpaRepository<User, Long> {

}
