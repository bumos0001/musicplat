package tw.musicplat.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import tw.musicplat.model.dto.UserDTO;
import tw.musicplat.tools.DTOConvertUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

/**
 *  登入成功的返回結果
 */
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 儲存用戶信息
        Object principal = authentication.getPrincipal();//獲取用戶身分信息
        Object credentials = authentication.getCredentials(); // 獲取憑證信息 (密碼)
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); //獲取權限信息

        UserDTO userDTO = DTOConvertUtil.toUserDTO((MyUserDetail) principal);

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap result = new HashMap();
        result.put("code",400); // 成功
        result.put("msg","成功"); // 成功

        result.put("data",userDTO); // 成功
        String json = objectMapper.writeValueAsString(result);

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}
