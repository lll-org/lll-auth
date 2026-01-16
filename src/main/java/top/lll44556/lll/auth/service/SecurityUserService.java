package top.lll44556.lll.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import top.lll44556.lll.auth.adapter.SecurityUser;


public interface SecurityUserService {

    SecurityUser findByUsername(String username);

    void createUser(UserDetails user);
}
