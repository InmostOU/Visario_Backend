package pro.inmost.amazon.chime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.inmost.amazon.chime.model.entity.Channel;

import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Channel findByChannelArn(String channelArn);

    @Query(value = "SELECT channel FROM Channel channel WHERE lower(channel.name) LIKE lower(concat('%', :channelName ,'%'))")
    List<Channel> findByName(String channelName);
}
