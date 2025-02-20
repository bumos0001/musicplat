package tw.musicplat.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tw.musicplat.model.dto.UserDTO;
import tw.musicplat.model.entity.User;

import java.util.Date;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User getUserById(Long id);
    User getUserByUsername(String username);
    User getUserByEmail(String email);

    @Query("""
        SELECT new tw.musicplat.model.dto.UserDTO(
            u.id, u.username, u.gender, u.birthday, u.email, u.phone, u.address,
            u.photo, u.enabled, u.created, u.modified, r.roleName
        )
        FROM User u
        LEFT JOIN u.roles r
        WHERE (:username IS NULL OR :username = '' OR u.username LIKE %:username%)
          AND (:email IS NULL OR :email = '' OR u.email LIKE %:email%)
          AND (:address IS NULL OR :address = '' OR u.address LIKE %:address%)
          AND (:phone IS NULL OR :phone = '' OR u.phone LIKE %:phone%)
    """)
    Page<UserDTO> findUsers(
            @Param("username") String username,
            @Param("email") String email,
            @Param("address") String address,
            @Param("phone") String phone,
            Pageable pageable
    );


}

