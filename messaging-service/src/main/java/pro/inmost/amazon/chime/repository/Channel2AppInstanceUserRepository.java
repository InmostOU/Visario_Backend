package pro.inmost.amazon.chime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.inmost.amazon.chime.model.entity.Channel2AppInstanceUser;

import java.util.List;

public interface Channel2AppInstanceUserRepository extends JpaRepository<Channel2AppInstanceUser, Long> {
    Channel2AppInstanceUser findByAppInstanceUserIdAndChannelId(Long appInstanceUserId, Long channelId);
    List<Channel2AppInstanceUser> findByAppInstanceUserId(Long appInstanceUserId);
}
