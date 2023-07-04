package ra.model.servicelmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.ERole;
import ra.model.entity.Roles;
import ra.model.reponsitory.RoleReponsitory;
import ra.model.service.RolesService;

import java.util.Optional;

@Service
public class RolesServicelmp implements RolesService {
    @Autowired
    private RoleReponsitory reponsitory;
    @Override
    public Optional<Roles> findByRoleName(ERole roleName) {

        return reponsitory.findByRoleName(roleName);
    }
}
