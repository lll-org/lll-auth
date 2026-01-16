package top.lll44556.lll.auth.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Component
@AllArgsConstructor
public class FrontendLoginEntryPoint implements AuthenticationEntryPoint {

    private final RequestCache requestCache;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String redirectUrl = request.getRequestURI() + "?" + request.getQueryString();
        requestCache.saveRequest(request, response);
        response.sendRedirect("http://auth.lll44556.top?redirect=" + redirectUrl);
    }
}
