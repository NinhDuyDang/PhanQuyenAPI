package ra.model.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.ERole;
import ra.model.entity.Roles;
import java.util.Optional;
@Repository
public interface RoleReponsitory extends JpaRepository<Roles, Integer> {
    Optional<Roles> findByRoleName(ERole roleName);



}
