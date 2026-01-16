package top.lll44556.lll.auth.tmp;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import top.lll44556.lll.auth.adapter.SecurityUser;
import top.lll44556.lll.auth.service.SecurityUserService;

@Component
@AllArgsConstructor
public class DBSecurityUserManager implements UserDetailsManager {
    private final SecurityUserService securityUserService;
    @Override
    public void createUser(UserDetails user) {
        securityUserService.createUser(user);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser securityUser = securityUserService.findByUsername(username);
        System.out.println("加载用户名和密码：");
        System.out.println(securityUser);
        return securityUser;
    }
}
