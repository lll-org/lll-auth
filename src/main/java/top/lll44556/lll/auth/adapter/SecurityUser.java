package top.lll44556.lll.auth.adapter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import top.lll44556.lll.auth.entity.User;

import java.util.Collection;
@AllArgsConstructor
@ToString
@Getter
public class SecurityUser implements UserDetails {
    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
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

    // 账号是否未过期, 默认是false
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    // 密码是否未过期, 默认是false
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    // 账号是否禁用，默认是false
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }


    public void setPassword(String password) {
        user.setPassword(password);
    }


    public void setUsername(String username) {
        user.setUsername(username);
    }

    public Long getId() {
        return user.getId();
    }

    public String getPhone() {
        return user.getPhone();
    }

    public String getEmail() {
        return user.getEmail();
    }
}
