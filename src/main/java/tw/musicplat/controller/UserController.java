package tw.musicplat.controller;


import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tw.musicplat.Service.UserService;
import tw.musicplat.model.dto.UserDTO;
import tw.musicplat.model.entity.User;
import tw.musicplat.tools.Result;

import java.util.Date;

@RestController
@RequestMapping("api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 根據ID查詢USER
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority({'ADMIN','USER','MANAGER'})")
    @GetMapping("/user/{id}")
    public Result getUser(@PathVariable int id) {
        UserDTO userByID = userService.getUserByID(id);
        if (userByID != null) {
            return Result.buildResult(200, "get user success",userByID);
        }else{
            return Result.buildResult(400, "get user failed",null);
        }
    }

    /**
     * 根據特定條件查詢符合條件的User
     * 如果沒傳入pageIndex,pageSize，則預設pageIndex = 1, pageSize = 10
     * @return 帶有分頁的UserDTO LIST
     */
    @PreAuthorize("hasAnyAuthority({'ADMIN','USER','MANAGER'})")
    @GetMapping("/user")
    public Result getUser(@RequestParam(required = false, defaultValue = "") String username,
                          @RequestParam(required = false, defaultValue = "") String email,
                          @RequestParam(required = false, defaultValue = "") String address,
                          @RequestParam(required = false, defaultValue = "") String phone,
                          @RequestParam(required = false, defaultValue = "0") Integer pageIndex,
                          @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                          @RequestParam(value = "sort",required = false, defaultValue = "id") String sortField,
                          @RequestParam(value = "direction",required = false, defaultValue = "ASC") String sortDirection) {

        Page<UserDTO> user = userService.findUser(username, email,address, phone, pageIndex, pageSize,sortField,sortDirection);
        if (user != null) {
            return Result.buildResult(200, "get user success",user);
        }
        return Result.buildResult(400, "get user failed",null);
    }

    /**
     * 傳入要修改的參數，根據參數修改User資料
     */
    @PutMapping("/user")
    public Result updateUser(@RequestParam Long userId,
                             @RequestParam(required = false, defaultValue = "") String username,
                             @RequestParam(required = false, defaultValue = "") String email,
                             @RequestParam(required = false, defaultValue = "") String address,
                             @RequestParam(required = false, defaultValue = "") String phone,
                             @RequestParam(required = false, defaultValue = "") String gender,
                             @RequestParam(required = false) Date birthday,
                             @RequestParam(required = false) MultipartFile image
                             ) {
        boolean b = userService.updateUser(userId, username, email, address, phone, gender, birthday, image);
        if (b){
            return Result.buildResult(200, "update user success",null);
        }else{
            return Result.buildResult(400, "update user failed",null);
        }
    }

}
