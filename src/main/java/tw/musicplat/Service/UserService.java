package tw.musicplat.Service;


import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tw.musicplat.Repository.UserRepository;
import tw.musicplat.config.DBUserDetailsManager;
import tw.musicplat.model.entity.User;

import java.util.List;

@Service
public class UserService {
    // 持久層
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Resource
    private DBUserDetailsManager userDetailsManager;

    @Resource
    private PasswordEncoder passwordEncoder;

    public User getUserByID(int id){
        User userById = userRepository.getUserById(id);
        System.out.println(userById);
        return userById;
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public boolean addUser(User newUser){
        User userByUsername = userRepository.getUserByUsername(newUser.getUsername());
        if (userByUsername != null){
            return false;
        }else{
            // 格式判斷
            // 加密
            String encodedPassword = passwordEncoder.encode(newUser.getPassword());
            newUser.setPassword(encodedPassword);
            newUser.setEnabled(true);
            userRepository.save(newUser);
            return true;
        }
    }
}
