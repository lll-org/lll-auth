package top.lll44556.lll.auth.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.lll44556.lll.auth.config.DBUserDetailsManager;
import top.lll44556.lll.auth.config.FederatedLoginSuccessHandler;
import top.lll44556.lll.auth.constant.RedisConstant;
import top.lll44556.lll.auth.entity.ExternalAccount;
import top.lll44556.lll.auth.entity.User;
import top.lll44556.lll.auth.service.ExternalAccountService;
import top.lll44556.lll.auth.vo.UserLoginVo;
import top.lll44556.lll.common.utils.R;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@AllArgsConstructor
@RequestMapping("lll/auth")
public class LoginController {

    private final DBUserDetailsManager dbUserDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final ExternalAccountService externalAccountService;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final DaoAuthenticationProvider authenticationManager;


//    @ResponseBody
//    @PostMapping("/login")
//    public R<Void> login(@RequestBody UserLoginVo userLoginVo, HttpServletRequest request, HttpServletResponse response) {
//        Authentication authentication =
//                authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(
//                                userLoginVo.getUsername(),
//                                userLoginVo.getPassword()
//                        )
//                );
//
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        context.setAuthentication(authentication);
//        // ⭐ 关键：保存到 Session
//        SecurityContextRepository repo =
//                new HttpSessionSecurityContextRepository();
//        repo.saveContext(context, request, response);
//        return R.ok();
//    }





    @GetMapping("/binding")
    public String binding() {
        return "redirect:/binding.html";
    }


    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        // TODO: key应当改为使用手机号，这里没有前端发送手机号，所以临时采用固定key
        BoundValueOperations<String, String> stringBoundValueOperations = stringRedisTemplate.boundValueOps(RedisConstant.getSmsCodeKey("lll"));
        System.out.println("lineCaptcha.getCode() = " + lineCaptcha.getCode().toLowerCase());
        System.out.println(lineCaptcha.getCode().getBytes().length);
        // todo: 开发阶段验证码固定为123456
//        stringBoundValueOperations.set(lineCaptcha.getCode(), 5 * 60, TimeUnit.SECONDS);
        stringBoundValueOperations.set("123456", 5 * 60, TimeUnit.SECONDS);
        lineCaptcha.write(response.getOutputStream());
    }

}
