package tw.musicplat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;

/**
 * 登入失敗的錯誤返回結果
 */
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        HashMap result = new HashMap();
        result.put("code",401);
        result.put("message","登入失敗");
        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(result);

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }
}
