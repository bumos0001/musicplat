package tw.musicplat.Service;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import tw.musicplat.config.DBUserDetailsManager;
import tw.musicplat.model.dto.LoginDTO;
import tw.musicplat.tools.JwtUtil;

import java.util.Map;

@Service
public class LoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    DBUserDetailsManager dbUserDetailsService;

    public String login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, password);
        authentication = authenticationManager.authenticate(authentication);
        return JwtUtil.generateToken(authentication);
    }
}
