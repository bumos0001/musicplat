package tw.musicplat.Service;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import tw.musicplat.config.DBUserDetailsManager;
import tw.musicplat.config.MyUserDetail;
import tw.musicplat.model.dto.LoginDTO;
import tw.musicplat.model.dto.UserDTO;
import tw.musicplat.tools.DTOConvertUtil;
import tw.musicplat.tools.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    DBUserDetailsManager dbUserDetailsService;

    public Map login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, password);
        authentication = authenticationManager.authenticate(authentication);
        UserDTO userDTO = DTOConvertUtil.toUserDTO((MyUserDetail) authentication.getPrincipal());
        HashMap<Object, Object> map = new HashMap<>();
        map.put("token",JwtUtil.generateToken(authentication));
        map.put("userInfo",userDTO);
        return map;
    }
}
