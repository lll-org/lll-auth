package top.lll44556.lll.auth.sms;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import top.lll44556.lll.auth.config.DBUserDetailsManager;
import top.lll44556.lll.auth.constant.RedisConstant;

import java.util.Objects;

@AllArgsConstructor
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private final DBUserDetailsManager dbUserDetailsManager;
    private final StringRedisTemplate stringRedisTemplate;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BoundValueOperations<String, String> stringBoundValueOperations = stringRedisTemplate.boundValueOps(RedisConstant.getSmsCodeKey("lll"));
        String captcha = stringBoundValueOperations.get() == null ? "" : stringBoundValueOperations.get();
        assert captcha != null;
        if (!Objects.equals(captcha.toLowerCase(), authentication.getCredentials().toString().toLowerCase())) {
            // todo: 编写exceptionHandler处理验证码错误
            throw new AuthenticationServiceException("验证码错误");
        }
        // loadUserByPhone中，若用户不存在则会创建用户
        UserDetails userDetails = dbUserDetailsManager.loadUserByPhone(authentication.getName());
        SmsAuthenticationToken smsAuthenticationToken = SmsAuthenticationToken.authenticated(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        smsAuthenticationToken.setDetails(authentication.getDetails());
        return smsAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (SmsAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
