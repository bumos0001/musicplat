package tw.musicplat.controller;


import org.springframework.web.bind.annotation.*;
import tw.musicplat.Service.UserService;
import tw.musicplat.model.dto.UserDTO;
import tw.musicplat.model.entity.User;
import tw.musicplat.tools.Result;

import java.util.List;

@RestController
@RequestMapping("api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public Result getUser(@PathVariable int id) {
        UserDTO userByID = userService.getUserByID(id);
        if (userByID != null) {
            return Result.buildResult(200, "get user success",userByID);
        }else{
            return Result.buildResult(400, "get user failed",null);
        }
    }

    @GetMapping("/user")
    public Result getAll() {
        List<UserDTO> allUser = userService.getAllUser();
        if (!allUser.isEmpty()) {
            return Result.buildResult(200, "get userList success", allUser);
        }else{
            return Result.buildResult(400, "get userList fail", null);

        }
    }

    @PostMapping("/user/add")
    public Result addUser(@RequestBody User user) {
        // 不能使用默認hibernate save方法
        boolean b = userService.addUser(user);
        if (b){
            return Result.buildResult(200, "add user success",user);
        }else{
            return Result.buildResult(400, "add user failed",null);
        }
    }
}
