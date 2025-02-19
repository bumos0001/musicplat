package tw.musicplat.controller;


import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.musicplat.Service.LoginService;
import tw.musicplat.model.dto.LoginDTO;
import tw.musicplat.tools.Result;

@RestController
@RequestMapping
public class LoginController {
    @Resource
    LoginService loginService;

    @PostMapping("login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        String token = loginService.login(loginDTO);
        if (token != null) {
            System.out.println(token);
            return Result.buildResult(200, "login success", token);
        } else {
            return Result.buildResult(400, "login fail", null);
        }
    }
}
