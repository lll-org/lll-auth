package top.lll44556.lll.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.lll44556.lll.auth.entity.User;

import java.util.List;

/**
* @author liululu
* @description 针对表【user】的数据库操作Service
* @createDate 2025-09-06 16:30:19
*/
public interface UserService extends IService<User> {


    List<String> selectRules(String username);
}
