package tw.musicplat.config;


import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity  // 開啟Security自定義配置(在SpringBoot項目中，可省略)
public class WebSecurityConfig {
    @Resource
    private DBUserDetailsManager myUserDetailsService;

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // 加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 開啟授權保護
        http
                // 授權配置
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/manager/**").hasAnyAuthority("MANAGER", "ADMIN") // 包括 ADMIN
                        .requestMatchers("/api/user/**").hasAnyAuthority("USER", "MANAGER", "ADMIN") // 包括 MANAGER 和 ADMIN
                        .requestMatchers("/**").permitAll()
                        // 除了以上的所有請求
                        .anyRequest()
                        //以認證請求自動授權
                        .authenticated()
                )
                // JWT過濾器 (jwt過濾器在前)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManager())

                .sessionManagement(sessionManagement -> {
                    sessionManagement
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                            .maximumSessions(1)
                            .expiredSessionStrategy(new MySessionInformationExpiredStrategy());
                })

                // 支持跨域訪問
                .cors(cors -> cors.configurationSource(createCorsConfig()))
                // 關閉csrf攻擊防禦
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

//    跨域
    private CorsConfigurationSource createCorsConfig(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173")); // 請求來源
        config.setAllowedHeaders(List.of("*")); // request header
        config.setAllowedMethods(List.of("*")); // http method

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(daoAuthenticationProvider());
        // 若有多個驗證方式，可使用：
        // List.of(daoAuthenticationProvider(), customAuthenticationProvider())
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider
                = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(myUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


}
