package tw.musicplat.Service;


import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tw.musicplat.Repository.UserRepository;
import tw.musicplat.config.DBUserDetailsManager;
import tw.musicplat.model.dto.UserDTO;
import tw.musicplat.model.entity.User;
import tw.musicplat.tools.DTOConvertUtil;

import java.util.ArrayList;
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

    public UserDTO getUserByID(int id){
        User userById = userRepository.getUserById(id);
        return DTOConvertUtil.toUserDTO(userById);
    }

    public List<UserDTO> getAllUser(){
        List<User> all = userRepository.findAll();
        if (all.isEmpty()) return null;
        ArrayList<UserDTO> allUser = new ArrayList<>();
        for (User user : all) {
            UserDTO userDTO = DTOConvertUtil.toUserDTO(user);
            allUser.add(userDTO);
        }
        return allUser;
    }

    public boolean addUser(User newUser){
        try{
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
        }catch(Exception e){
            return false;
        }
    }
}
