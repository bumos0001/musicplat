package tw.musicplat.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tw.musicplat.Repository.UserRepository;
import tw.musicplat.model.entity.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Service
public class RegisterService {
    private UserRepository userRepository;

    @Value("${photo.storage.prefix}")
    private String photoStoragePrefix;


    public RegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
            Path path = Paths.get(photoStoragePrefix,"user", uuid.toString() + ".jpg");
            Path databasePath = Paths.get("user",uuid.toString() + ".jpg");
            image.transferTo(path);

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setBirthday(birthday);
            user.setEmail(email);
            user.setAddress(address);
            user.setPhone(phone);
            user.setGender(gender);
            user.setEnabled(true);
            user.setPhoto(databasePath.toString());
            User save = userRepository.save(user);

            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
