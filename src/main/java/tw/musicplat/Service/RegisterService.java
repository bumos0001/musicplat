package tw.musicplat.Service;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tw.musicplat.Repository.RoleRepository;
import tw.musicplat.Repository.UserRepository;
import tw.musicplat.model.entity.Role;
import tw.musicplat.model.entity.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

@Service
public class RegisterService {
    private final PasswordEncoder passwordEncoder;
    @Resource
    private UserRepository userRepository;
    @Resource
    private RoleRepository roleRepository;

    @Value("${photo.storage.prefix}")
    private String photoStoragePrefix;


    public RegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public boolean register(String username,
                            String password,
                            Date birthday,
                            String email,
                            String address,
                            String phone,
                            MultipartFile image,
                            String gender) {
        try{
            UUID uuid = UUID.randomUUID();
            Path path = Paths.get(photoStoragePrefix,"user", uuid + ".jpg");
            String databasePath = Paths.get("img","user", uuid + ".jpg").toString().replace('\\','/');
            image.transferTo(path);
//          創建使用者
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setBirthday(birthday);
            user.setEmail(email);
            user.setAddress(address);
            user.setPhone(phone);
            user.setGender(gender);
            user.setEnabled(true);
            user.setPhoto(databasePath.toString());

//            角色設定
            Role roleUser = roleRepository.findByRoleName("USER");
            HashSet<Role> roleSet = new HashSet<>();
            roleSet.add(roleUser);
            user.setRoles(roleSet);
//            保存
            User save = userRepository.save(user);

            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
