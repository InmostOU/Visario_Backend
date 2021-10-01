package pro.inmost.amazon.chime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import pro.inmost.amazon.chime.model.entity.User;

@Repository
public interface AppInstanceUserRepository extends JpaRepository<AppInstanceUser, Long> {
    AppInstanceUser findByUser(User user);
}
