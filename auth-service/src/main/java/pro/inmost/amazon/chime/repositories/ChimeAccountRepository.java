package pro.inmost.amazon.chime.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.inmost.amazon.chime.model.entity.ChimeAccount;

@Repository
public interface ChimeAccountRepository extends JpaRepository<ChimeAccount, Long> {
    ChimeAccount findByName(String name);
}
