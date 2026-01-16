package top.lll44556.lll.auth.mapper;

import top.lll44556.lll.auth.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author liululu
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-09-06 16:30:19
* @Entity top.lll44556.gulimall.auth.entity.User
*/
public interface UserMapper extends BaseMapper<User> {

    List<String> selectRules(String username);
}




