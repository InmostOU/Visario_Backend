package pro.inmost.amazon.chime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.inmost.amazon.chime.model.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
