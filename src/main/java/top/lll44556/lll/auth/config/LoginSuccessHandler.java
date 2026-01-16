package top.lll44556.lll.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import top.lll44556.lll.common.utils.R;

import java.io.IOException;

@Component
@AllArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper  objectMapper;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            // 登录成功后，回到 continue（也就是原 /oauth2/authorize...）
//            String cont = request.getParameter("continue");
//            if (cont == null || cont.isBlank()) {
//                cont = "http://192.1681.1.100:11001/api/hello";
//            }
//            response.sendRedirect(cont);

        String s = objectMapper.writeValueAsString(R.ok());
        response.getWriter().write(s);
    }
}
