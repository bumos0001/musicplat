package tw.musicplat.config;


import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tw.musicplat.model.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@EqualsAndHashCode
public class MyUserDetail implements UserDetails {
    private final User user;
    public MyUserDetail(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        if (!user.getRoles().isEmpty()){
            authorities.add(new SimpleGrantedAuthority(user.getRoles().iterator().next().getRoleName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
    public Long getId(){
        return user.getId();
    }

    public String getGender(){
        return user.getGender();
    }

    public Date getBirthday(){
        return user.getBirthday();
    }
    public String getEmail(){
        return user.getEmail();
    }
    public String getPhone(){
        return user.getPhone();
    }
    public String getAddress(){
        return user.getAddress();
    }
    public String getPhoto(){
        return user.getPhoto();
    }
    public Date getCreated(){
        return user.getCreated();
    }
    public Date getModified(){
        return user.getModified();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
