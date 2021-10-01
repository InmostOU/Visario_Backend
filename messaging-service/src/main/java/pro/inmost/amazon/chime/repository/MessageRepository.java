package pro.inmost.amazon.chime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import pro.inmost.amazon.chime.model.entity.Channel;
import pro.inmost.amazon.chime.model.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChannel(Channel channel);

    Optional<Message> findByMessageId(String messageId);

    List<Message> findByAppInstanceUserAndWithAttachment(AppInstanceUser user, Boolean withAttachments);
}
