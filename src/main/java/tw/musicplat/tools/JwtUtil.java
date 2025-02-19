package tw.musicplat.tools;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import tw.musicplat.config.MyUserDetail;

import java.util.*;

public class JwtUtil {
    private static final String SECRET = "gry5h61ty5156ty1hnte1n1tyn56heh515615hjgo";
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

    // 解析Token，返回claim
    public static Claims getClaims(String token){
        JwtParser parser = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes())).build();
        return parser.parseSignedClaims(token).getPayload();
    }

    public static String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    public static String getValue(String token, String key) {
        return (String) getClaims(token).get(key);
    }

    public static Boolean isTokenValid(String token) {
        getSubject(token); // 若 token 有任何異常，則由 jjwt 套件直接拋出錯誤。
        return true; // 能走到回傳表示驗證通過，token 合法
    }

}
