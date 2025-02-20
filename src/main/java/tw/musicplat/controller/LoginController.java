package tw.musicplat.controller;


import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.musicplat.Service.LoginService;
import tw.musicplat.model.dto.LoginDTO;
import tw.musicplat.tools.Result;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class LoginController {
    @Resource
    LoginService loginService;

    @PostMapping("login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        Map login = loginService.login(loginDTO);
        if (login.get("token") != null) {
            return Result.buildResult(200, "login success", login);
        } else {
            return Result.buildResult(400, "login fail", null);
        }
    }
}
