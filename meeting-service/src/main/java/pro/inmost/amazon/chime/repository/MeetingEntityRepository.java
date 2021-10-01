package pro.inmost.amazon.chime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.inmost.amazon.chime.model.entity.MeetingEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingEntityRepository extends JpaRepository<MeetingEntity, Long> {

    List<MeetingEntity> findByMeetingId(String meetingId);

    @Transactional
    void deleteByMeetingId(String meetingHost);

    Optional<MeetingEntity> findByMeetingIdAndUserId(String meetingId, Long userId);
}
