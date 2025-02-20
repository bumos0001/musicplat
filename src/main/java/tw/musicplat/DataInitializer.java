package tw.musicplat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tw.musicplat.Repository.RoleRepository;
import tw.musicplat.Repository.UserRepository;
import tw.musicplat.model.entity.Role;
import tw.musicplat.model.entity.User;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${photo.storage.prefix}")
    private String photoStoragePrefix;

    @Value("${video.storage.prefix}")
    private String videoStoragePrefix;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
//        建立角色、使用者
        Role adminRole = roleRepository.findByRoleName("ADMIN");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setRoleName("ADMIN");
            adminRole.setRoleDescription("最高管理員");
            roleRepository.save(adminRole);
        } else {
            // 使用持久化上下文中的實體
            adminRole = roleRepository.getOne(adminRole.getRoleId());
        }

        User adminUser = userRepository.getUserByUsername("admin");
        if (adminUser == null) {
            adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setEmail("admin@example.com");
            adminUser.setEnabled(true);

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminUser.setRoles(adminRoles);
            userRepository.save(adminUser);
        }

//        建立資料夾目錄
        try{
            Path songImagePath = Paths.get(photoStoragePrefix,"song");
            Path userImagePath = Paths.get(photoStoragePrefix,"user");
            Path songVideoPath = Paths.get(videoStoragePrefix,"song");

            Files.createDirectories(songImagePath);
            Files.createDirectories(userImagePath);
            Files.createDirectories(songVideoPath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
