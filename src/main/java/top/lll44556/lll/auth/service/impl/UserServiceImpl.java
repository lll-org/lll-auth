package top.lll44556.lll.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.lll44556.lll.auth.entity.User;
import top.lll44556.lll.auth.service.UserService;
import top.lll44556.lll.auth.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author liululu
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-09-06 16:30:19
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{


    @Override
    public List<String> selectRules(String username) {
        return baseMapper.selectRules(username);
    }
}




