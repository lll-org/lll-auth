package top.lll44556.lll.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.lll44556.lll.auth.adapter.SecurityUser;

public interface SecurityUserMapper extends BaseMapper<SecurityUser> {
    SecurityUser selectByUsername(String username);
}
