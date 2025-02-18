package tw.musicplat.controller;


import org.springframework.web.bind.annotation.*;
import tw.musicplat.Service.UserService;
import tw.musicplat.model.entity.User;

import java.util.List;

@RestController
@RequestMapping("api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable int id) {
        User userByID = userService.getUserByID(id);
        return userByID.toString();
    }

    @GetMapping("/user")
    public String getAll() {
        List<User> allUser = userService.getAllUser();
        return allUser.toString();
    }

    @PostMapping("/user/add")
    public String addUser(@RequestBody User user) {
        // 不能使用默認hibernate save方法
        userService.addUser(user);
        return user.toString();
    }
}
