package pro.inmost.amazon.chime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pro.inmost.amazon.chime.model.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
