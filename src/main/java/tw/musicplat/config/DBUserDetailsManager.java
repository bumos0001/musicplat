package tw.musicplat.config;

import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import tw.musicplat.Repository.UserRepository;
import tw.musicplat.model.entity.User;

import java.util.ArrayList;

/**
 * 使用者資料
 */
@Component
public class DBUserDetailsManager implements UserDetailsService {
    @Resource
    private UserRepository userRepository;
    // 最重要的方法
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userByUsername = userRepository.getUserByUsername(username);
        if (userByUsername == null) {
            throw new UsernameNotFoundException(username);
        }else{
            // 獲取使用者角色
            return new MyUserDetail(userByUsername);
        }
    }
}
