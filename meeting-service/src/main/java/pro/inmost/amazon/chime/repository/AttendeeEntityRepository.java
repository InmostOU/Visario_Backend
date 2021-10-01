package pro.inmost.amazon.chime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.inmost.amazon.chime.model.entity.AttendeeEntity;

import javax.transaction.Transactional;
import java.util.Optional;

public interface AttendeeEntityRepository extends JpaRepository<AttendeeEntity, Long> {

    Optional<AttendeeEntity> findByUserId(String userId);

    Optional<AttendeeEntity> findByMeetingIdAndUserId(String meetingId, String userId);

    @Transactional
    void deleteByMeetingIdAndUserId(String meetingId, String userId);
}
