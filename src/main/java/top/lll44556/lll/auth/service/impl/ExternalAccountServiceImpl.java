package top.lll44556.lll.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.lll44556.lll.auth.config.DBUserDetailsManager;
import top.lll44556.lll.auth.config.FederatedLoginSuccessHandler;
import top.lll44556.lll.auth.entity.ExternalAccount;
import top.lll44556.lll.auth.entity.User;
import top.lll44556.lll.auth.service.ExternalAccountService;
import top.lll44556.lll.auth.mapper.ExternalAccountMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
* @author liululu
* @description 针对表【external_account】的数据库操作Service实现
* @createDate 2025-11-11 11:52:16
*/
@Service
@AllArgsConstructor
public class ExternalAccountServiceImpl extends ServiceImpl<ExternalAccountMapper, ExternalAccount>
    implements ExternalAccountService{

    private final PasswordEncoder passwordEncoder;
    private final DBUserDetailsManager dbUserDetailsManager;

    @Override
    public void saveExternalAccount() throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
            String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(); // 获取客户端id
            OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
            Map<String, Object> attributes = oAuth2User.getAttributes();


            User user = new User();
            user.setUsername((String)attributes.get("login"));
            // 默认密码设置为用户名
            user.setPassword(passwordEncoder.encode((String)attributes.get("login")));
            dbUserDetailsManager.createUser(user);

            ExternalAccount externalAccount = getExternalAccount(oAuth2AuthenticationToken, oAuth2User, user);
            baseMapper.insert(externalAccount);
        } else {
            // 非oauth2 第三方登录
            throw new RuntimeException("不支持的认证方式");
        }
    }


    private ExternalAccount getExternalAccount(OAuth2AuthenticationToken oAuth2AuthenticationToken, OAuth2User oAuth2User, User user) throws JsonProcessingException {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        ObjectMapper objectMapper = new ObjectMapper();
        Integer id = (Integer)attributes.get("id");
        String json = objectMapper.writeValueAsString(attributes);
        ExternalAccount externalAccount = new ExternalAccount();
        externalAccount.setId(Long.valueOf(id));
        externalAccount.setUserId(user.getId());
        externalAccount.setProvider(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
        externalAccount.setExtraJson(json);
        return externalAccount;
    }
}




