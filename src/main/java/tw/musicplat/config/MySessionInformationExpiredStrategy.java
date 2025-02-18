package tw.musicplat.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import java.io.IOException;
import java.util.HashMap;

/**
 * 重複登入的錯誤返回結果
 */
public class MySessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap result = new HashMap();
        result.put("code",-1); // 成功
        result.put("msg","該帳號已從其他設備登入"); // 成功
        String json = objectMapper.writeValueAsString(result);

        HttpServletResponse response = event.getResponse();

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}
