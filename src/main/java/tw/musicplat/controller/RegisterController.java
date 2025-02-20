package tw.musicplat.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tw.musicplat.Repository.UserRepository;
import tw.musicplat.Service.RegisterService;
import tw.musicplat.Service.UserService;
import tw.musicplat.model.entity.User;
import tw.musicplat.tools.Result;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@RestController
@MultipartConfig
@RequestMapping("api")
public class RegisterController {
    @Resource
    private UserRepository userRepository;
    @Resource
    private RegisterService registerService;


    /**
     * 傳入username，返回username是否可用
     * @param username
     * @return
     */
    @GetMapping("/register/{username}")
    public Result checkUserIsExist(@PathVariable String username) {
        User userByUsername = userRepository.getUserByUsername(username);
        if (userByUsername == null) {
            return Result.buildResult(200, "username can use",true);
        }else{
            return Result.buildResult(400, "username cant use",false);
        }
    }

    /**
     * 檢查email是否重複
     * @return
     */
    @GetMapping("/register/check-email/{email}")
    public Result checkEmailIsExist(@PathVariable String email) {
        User userByEmail = userRepository.getUserByEmail(email);
        if (userByEmail == null) {
            return Result.buildResult(200, "email can use",true);
        }
        else{
            return Result.buildResult(400, "username can't use",false);
        }
    }

    /**
     * 接收註冊表單參數，插入資料
     */
    @PostMapping("register")
    public Result register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam Date birthday,
                           @RequestParam String email,
                           @RequestParam String address,
                           @RequestParam String phone,
                           @RequestParam MultipartFile image,
                           @RequestParam String gender) {
        boolean register = registerService.register(username, password, birthday, email, address, phone, image, gender);
        if (register) {
            return Result.buildResult(200, "register success",true);
        }
        else{
            return Result.buildResult(400, "register fail",false);
        }


    }
}
