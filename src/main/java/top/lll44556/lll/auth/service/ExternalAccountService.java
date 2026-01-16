package top.lll44556.lll.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import top.lll44556.lll.auth.entity.ExternalAccount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author liululu
* @description 针对表【external_account】的数据库操作Service
* @createDate 2025-11-11 11:52:16
*/
public interface ExternalAccountService extends IService<ExternalAccount> {

    void saveExternalAccount() throws JsonProcessingException;
}
