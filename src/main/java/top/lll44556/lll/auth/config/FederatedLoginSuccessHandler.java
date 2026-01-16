package top.lll44556.lll.auth.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.lll44556.lll.auth.constant.URLConstant;
import top.lll44556.lll.auth.entity.ExternalAccount;
import top.lll44556.lll.auth.entity.User;
import top.lll44556.lll.auth.service.ExternalAccountService;

import java.io.IOException;
import java.util.Map;

public class FederatedLoginSuccessHandler implements AuthenticationSuccessHandler {

    public static final String ATTR_CONTINUE_URL = "OAUTH2_AUTHZ_CONTINUE_URL";
    private final RequestCache requestCache;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final ExternalAccountService externalAccountService;


    public FederatedLoginSuccessHandler(RequestCache requestCache, ExternalAccountService externalAccountService) {
        this.requestCache = requestCache;
        this.externalAccountService = externalAccountService;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        System.out.println("第三方 oauth2获取授权成功");
        // 创建本地账户，并关联第三方账号信息
        if (authentication instanceof OAuth2AuthenticationToken oauth2AuthenticationToken) {
            // oauth2AuthenticationToken 可以直接从SecurityContextHolder中获取，所以这里可以不用传递参数
            Long id = Long.valueOf((Integer)oauth2AuthenticationToken.getPrincipal().getAttributes().get("id"));
            ExternalAccount externalAccount = externalAccountService.getById(id);
            if (externalAccount == null) {
                // 本地账户未创建，绑定本地账号
                externalAccountService.saveExternalAccount();
            }

        }


        SavedRequest savedRequest = this.requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String continueUrl = savedRequest.getRedirectUrl();
            System.out.println("original continueUrl = " + continueUrl);
            if (continueUrl != null && continueUrl.endsWith("&continue")) {
                continueUrl = continueUrl.substring(0, continueUrl.length() - "&continue".length());
            }
            System.out.println("new continueUrl = " + continueUrl);
            request.getSession(true).setAttribute(ATTR_CONTINUE_URL, continueUrl);
            // 重定向到确认授权页面
            this.redirectStrategy.sendRedirect(request, response, continueUrl);
            return;
        }
        System.out.println("savedRequest 为 null");
        // 重定向到绑定本地账号页面
        this.redirectStrategy.sendRedirect(request, response, URLConstant.RequestBindingURL);
    }
}
