package pro.inmost.amazon.chime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.inmost.amazon.chime.model.entity.User;
import pro.inmost.amazon.chime.model.entity.UserAvatar;

import java.util.List;
import java.util.Optional;

public interface UserAvatarRepository extends JpaRepository<UserAvatar, Long> {
    Optional<UserAvatar> findByUser(User user);

    Optional<List<UserAvatar>> findAllByUser(User user);
}
