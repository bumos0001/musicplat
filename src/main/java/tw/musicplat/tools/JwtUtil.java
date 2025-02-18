package tw.musicplat.tools;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import tw.musicplat.config.MyUserDetail;

import java.util.*;

public class JwtUtil {
    @Value("${jwtToken.secret}")
    private static String SECRET;
    private static final int EXPIRE_TIME = 60 * 60 ;

    // 產生token
    public static String generateToken(Authentication authentication) {
        MyUserDetail principal = (MyUserDetail)authentication.getPrincipal();
        // payload
        System.out.println("authentication.getAuthorities():"+authentication.getAuthorities());

        HashMap<String, Object> map = new HashMap<>();
        map.put("username", principal.getUsername());
        map.put("roles", principal.getAuthorities());

        return Jwts.builder() // 使用流化的建構器
                .claims(map)
                .subject(principal.getId()+"") // 設定主承載內容,通常是唯一識別id
                .issuedAt(new Date()) // 發行時間
                .expiration(new Date(System.currentTimeMillis() + EXPIRE_TIME * 1000L)) // 到期時間
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes())) // 密鑰簽名
                .compact(); // 產生出JWT token
    }
}
