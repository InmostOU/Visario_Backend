package pro.inmost.amazon.chime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.inmost.amazon.chime.model.entity.ConversationToAppInstanceUser;

public interface ConversationToAppInstanceUserRepository extends JpaRepository<ConversationToAppInstanceUser, Long> {
}
