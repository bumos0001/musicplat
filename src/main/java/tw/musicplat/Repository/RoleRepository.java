package tw.musicplat.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tw.musicplat.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(String roleAdmin);
}
