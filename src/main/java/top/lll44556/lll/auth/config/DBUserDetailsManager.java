package top.lll44556.lll.auth.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import top.lll44556.lll.auth.adapter.SecurityUser;
import top.lll44556.lll.auth.entity.User;
import top.lll44556.lll.auth.mapper.UserMapper;
import top.lll44556.lll.auth.service.UserService;

import java.util.ArrayList;
import java.util.Collection;

@Component
//@AllArgsConstructor
public class DBUserDetailsManager implements UserDetailsManager {

    @Autowired
     public UserMapper userMapper;
    @Autowired
    public PasswordEncoder passwordEncoder;
    @Autowired
    public UserService userService;

//    @Override
    public void createUser(User user) {
        userService.save(user);
    }

    @Override
    public void createUser(UserDetails user) {

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
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        // 基于用户名查询失败，抛出异常
        // todo: 自定义异常处理
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getUsername().equals("wholike")) {
//            authorities.add(new SimpleGrantedAuthority("USER_LIST"));
            authorities.add(new SimpleGrantedAuthority("USER_SAVE"));
        }

        return new SecurityUser(user, authorities);
    }

    // 手机号登录，根据手机号获取用户信息；如果用户不存在，则创建新用户
    public UserDetails loadUserByPhone(String phone) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在，则创建新用户
        if (user == null) {
            user = new User();
            user.setUsername(phone);
            user.setPassword(passwordEncoder.encode(""));
            user.setPhone( phone);
            createUser(user);

        }
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("openid"));
        authorities.add(new SimpleGrantedAuthority("profile"));
        return new SecurityUser(user, authorities);
    }
}
