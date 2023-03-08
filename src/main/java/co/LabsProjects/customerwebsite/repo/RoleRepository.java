package co.LabsProjects.customerwebsite.repo;

import co.LabsProjects.customerwebsite.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role getByRole(Role.Roles role);
}
