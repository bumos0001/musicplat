package tw.musicplat.Service;


import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tw.musicplat.Repository.UserRepository;
import tw.musicplat.config.DBUserDetailsManager;
import tw.musicplat.model.dto.UserDTO;
import tw.musicplat.model.entity.User;
import tw.musicplat.tools.DTOConvertUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Resource
    private UserRepository userRepository;

    @Resource
    private DBUserDetailsManager userDetailsManager;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Value("${photo.storage.prefix}")
    private String photoStoragePrefix;

    /**
     * 傳入UserId，返回UserDTO對象
     * @param id userID
     * @return UserDTO
     */
    public UserDTO getUserByID(int id){
        User userById = userRepository.getUserById(Long.parseLong(String.valueOf(id)));
        return DTOConvertUtil.toUserDTO(userById);
    }

    /**
     * 返回所有使用者
     * @return
     */
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

    /**
     * 傳入username，透過username查詢user
     * @param username
     * @return
     */
    public UserDTO getUserByUsername(String username){
        User userByUsername = userRepository.getUserByUsername(username);
        if (userByUsername == null) return null;
        else{
            return DTOConvertUtil.toUserDTO(userByUsername);
        }
    }

    /**
     * 傳入條件查詢的參數進行USER查詢
     */
    public Page<UserDTO> findUser(String username, String email,String address, String phone, int page, int size,String sortField, String sortDirection) {
        // 處理排序
        Sort sort = Sort.by(Sort.Order.by(sortField));
        if ("DESC".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        // 處理分頁
        Pageable pageable = PageRequest.of(page, size, sort);
        // 撈取資料
        Page<UserDTO> usersByUsernameAndEmail = userRepository.findUsers(username, email, address, phone, pageable);
        System.out.println(usersByUsernameAndEmail.getContent());
        return usersByUsernameAndEmail;
    }

    /**
     *  傳入修改的條件，根據條件修改User資料
     */
    public boolean updateUser(Long userId,
                              String username,
                              String email,
                              String address,
                              String phone,
                              String gender,
                              Date birthday,
                              MultipartFile image) {
        try{
            // 根據ID檢查有無此User
            User user = userRepository.getUserById(userId);
            if (user == null) return false;

            // 根據是否有傳入條件做對應修改
            if (username != null && !username.isEmpty()) user.setUsername(username);
            if (email != null && !email.isEmpty()) user.setEmail(email);
            if (address != null && !address.isEmpty()) user.setAddress(address);
            if (phone != null && !phone.isEmpty()) user.setPhone(phone);
            if (gender != null && !gender.isEmpty()) user.setGender(gender);
            if (birthday != null) user.setBirthday(birthday);

            // 儲存圖片檔案
            if (image != null && !image.isEmpty()){
                // 刪除舊檔案
                boolean b = Files.deleteIfExists(Paths.get(photoStoragePrefix, user.getPhoto().replace("img", "")));
                if (b){
                    System.out.println("刪除成功");
                }
                // 新的檔案儲存
                UUID uuid = UUID.randomUUID();
                Path path = Paths.get(photoStoragePrefix,"user", uuid + ".jpg");
                String databasePath = Paths.get("img","user", uuid + ".jpg").toString().replace('\\','/');
                image.transferTo(path);
                user.setPhoto(databasePath);
            }

            User save = userRepository.save(user);
            return true;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
