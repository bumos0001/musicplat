package tw.musicplat.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.musicplat.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserById(int id);
    User getUserByUsername(String username);

    User getUserByEmail(String email);
}
