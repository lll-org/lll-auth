package top.lll44556.lll.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import top.lll44556.lll.auth.adapter.SecurityUser;
import top.lll44556.lll.auth.mapper.SecurityUserMapper;
import top.lll44556.lll.auth.mapper.UserMapper;
import top.lll44556.lll.auth.service.SecurityUserService;

@Service
@AllArgsConstructor
public class SecurityUserServiceImpl extends ServiceImpl<SecurityUserMapper, SecurityUser> implements SecurityUserService {

    private final UserMapper userMapper;

    @Override
    public SecurityUser findByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }

    @Override
    public void createUser(UserDetails user) {
        SecurityUser securityUser = (SecurityUser) user;
        userMapper.insert(securityUser.getUser());
    }
}
