package pro.inmost.amazon.chime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.inmost.amazon.chime.model.entity.Contacts;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
public interface ContactsRepository extends JpaRepository<Contacts, Long> {

    @Query(value = "SELECT * FROM Contacts WHERE  OWNER_ID=:id", nativeQuery = true)
    Set<Contacts> findAllByOwnerId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE  FROM Contacts WHERE  USER_ID=:id", nativeQuery = true)
    void deleteByUserId(@Param("id") Long id);

    @Query(value = "SELECT * FROM Contacts WHERE  OWNER_ID=:ownerId and USER_ID=:userId",nativeQuery = true)
    Contacts findByOwnerIdAndUserId(@Param("ownerId") Long ownerId, @Param("userId") Long userId);

    void deleteById(Long id);
}
