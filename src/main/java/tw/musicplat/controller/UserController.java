package tw.musicplat.controller;


import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tw.musicplat.Service.UserService;
import tw.musicplat.model.dto.UserDTO;
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
     * @param id userID
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
     *      * 根據特定條件查詢符合條件的User
     *      * 如果沒傳入pageIndex,pageSize，則預設pageIndex = 1, pageSize = 10
     *      * @return 帶有分頁的UserDTO LIST
     * @param username username
     * @param email email
     * @param address address
     * @param phone phone
     * @param enabled 軟刪除標識
     * @param pageIndex 頁碼
     * @param pageSize 頁容量
     * @param sortField 排序欄位
     * @param sortDirection ASC DESC 升降序
     */
    @PreAuthorize("hasAnyAuthority({'ADMIN','MANAGER'})")
    @GetMapping("/user")
    public Result getUser(@RequestParam(required = false, defaultValue = "") String username,
                          @RequestParam(required = false, defaultValue = "") String email,
                          @RequestParam(required = false, defaultValue = "") String address,
                          @RequestParam(required = false, defaultValue = "") String phone,
                          @RequestParam(required = false) Boolean enabled,
                          @RequestParam(required = false, defaultValue = "0") Integer pageIndex,
                          @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                          @RequestParam(value = "sort",required = false, defaultValue = "id") String sortField,
                          @RequestParam(value = "direction",required = false, defaultValue = "ASC") String sortDirection) {
        Page<UserDTO> user = userService.findUser(username, email,address, phone, enabled, pageIndex, pageSize,sortField,sortDirection);
        if (user != null) {
            return Result.buildResult(200, "get user success",user);
        }
        return Result.buildResult(400, "get user failed",null);
    }


    /**
     * 傳入要修改的參數，根據參數修改User資料
     * @param userId userID
     * @param username username
     * @param email email
     * @param address address
     * @param phone phone
     * @param gender gender
     * @param birthday birthday
     * @param image 使用者照片
     */
    @PutMapping("/user")
    @PreAuthorize("hasAnyAuthority({'ADMIN','USER','MANAGER'})")
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

    /**
     * 使用者啟用
     */
    @PatchMapping("/user/{id}/enable")
    @PreAuthorize("hasAnyAuthority({'ADMIN','MANAGER'})")
    public Result enableUser(@PathVariable Long id){
        boolean b = userService.enableUser(id);
        if (b){
            return Result.buildResult(200, "enable user success",null);
        }else{
            return Result.buildResult(400, "enable user failed",null);
        }

    }

    /**
     * 使用者禁用
     */
    @PatchMapping("/user/{id}/disable")
    @PreAuthorize("hasAnyAuthority({'ADMIN','MANAGER'})")
    public Result disableUser(@PathVariable Long id){
        boolean b = userService.disableUser(id);
        if (b){
            return Result.buildResult(200, "enable user success",null);
        }else{
            return Result.buildResult(400, "enable user failed",null);
        }
    }

}
