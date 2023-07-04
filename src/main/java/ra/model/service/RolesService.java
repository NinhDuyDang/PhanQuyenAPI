package ra.model.service;

import ra.model.entity.ERole;
import ra.model.entity.Roles;

import java.util.Optional;

public interface RolesService {
    Optional<Roles> findByRoleName(ERole roleName);
}
