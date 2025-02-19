package tw.musicplat.config;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import tw.musicplat.tools.JwtUtil;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private DBUserDetailsManager myUserDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtAuthenticationFilter(HandlerExceptionResolver handlerExceptionResolver, DBUserDetailsManager myUserDetailsService) {
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.myUserDetailsService = myUserDetailsService;
    }

    private static final String HEADER_AUTH = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 從 headers 中取得 Authorization header
        String authHeader = request.getHeader(HEADER_AUTH);
        // 若 http 請求的 headers 中不包含 Authorization，或包含但不合法，直接交由 Spring Security 處理
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 提取 jwt token
        final String jwtToken = authHeader.substring(7);

        // 檢驗 jwt 是否有效，若解析過程中出現錯誤，則會由 jwtTool（基於 jjwt 實現）拋出異常。
        // 捕獲異常後，轉發給 GlobalExceptionHandler，並在那定義回應狀態碼。
        try {
            JwtUtil.isTokenValid(jwtToken);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }

        // 透過token取得使用者資料
        Claims claims = JwtUtil.getClaims(jwtToken);
        UserDetails myUser = myUserDetailsService.loadUserByUsername(claims.get("username", String.class));

        // 給予權限
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        myUser.getUsername(), myUser.getPassword(), myUser.getAuthorities());
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
