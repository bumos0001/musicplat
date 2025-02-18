package tw.musicplat.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity  // 開啟Security自定義配置(在SpringBoot項目中，可省略)
public class WebSecurityConfig {
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
                // 登入配置
                .formLogin(form -> {
                            form.loginPage("/login").permitAll()
                            .successHandler(new MyAuthenticationSuccessHandler()) // 認證成功時的處理
                            .failureHandler(new MyAuthenticationFailureHandler()); // 認證失敗的處理
                        }
                )
                // 登出配置
                .logout(logout -> {
                    logout.logoutSuccessHandler(new MyLogoutSuccessHandler()); // 登出成功時的處理
                })
                // 錯誤處理
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(new MyAuthenticationEntryPoint()); //請求未驗證的處理
                    exception.accessDeniedHandler(new MyAccessDeniedHandler());
                })
                //
                .sessionManagement(sessionManagement -> {
                    sessionManagement.maximumSessions(1).expiredSessionStrategy(new MySessionInformationExpiredStrategy());
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
}
