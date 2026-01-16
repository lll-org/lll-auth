package top.lll44556.lll.auth.config;


import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import top.lll44556.lll.auth.adapter.SecurityUser;
import top.lll44556.lll.auth.entity.ExternalAccount;
import top.lll44556.lll.auth.service.ExternalAccountService;
import top.lll44556.lll.auth.service.UserService;
import top.lll44556.lll.auth.sms.SmsAuthenticationFilter;
import top.lll44556.lll.auth.sms.SmsAuthenticationProvider;
import top.lll44556.lll.auth.sms.SmsAuthenticationToken;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

//     统一的 RequestCache：用来拿到 /oauth2/authorize 的 SavedRequest
    @Bean
    public RequestCache requestCache() {
        return new HttpSessionRequestCache();
    }

    @Bean
    public FederatedLoginSuccessHandler federatedLoginSuccessHandler(RequestCache requestCache, ExternalAccountService externalAccountService) {
        return new FederatedLoginSuccessHandler(requestCache, externalAccountService);
    }

    @Bean
    public AuthenticationSuccessHandler formLoginSuccessHandler(RequestCache requestCache) {
        SavedRequestAwareAuthenticationSuccessHandler handler =
                new SavedRequestAwareAuthenticationSuccessHandler();
        handler.setRequestCache(requestCache);
        return handler;
    }


    // 用于协议端点的 Spring Security 过滤器链。作为认证服务器的入口
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http,
            FrontendLoginEntryPoint frontendLoginEntryPoint
    ) throws Exception {


        // 配置授权服务器
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());
        http.securityMatcher("/oauth2/authorize", "/oauth2/token", "/oauth2/logout", "oauth2/jwks");


        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(frontendLoginEntryPoint));

        // 接受用户信息和/或客户端注册的访问令牌
        http.oauth2ResourceServer((resourceServer) ->
                        resourceServer.jwt(Customizer.withDefaults()));

        return http.build();
    }

    // 用于身份验证的 Spring Security 过滤器链。
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          FederatedLoginSuccessHandler federatedLoginSuccessHandler,
                                                          SmsAuthenticationFilter smsAuthenticationFilter,
                                                          LoginSuccessHandler loginSuccessHandler,
                                                          LoginFailureHandler loginFailureHandler
                                                          ) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login","/login/**", "/login.html", "/smsLogin/**").permitAll()
                .requestMatchers("/lll/auth/**").permitAll()
                .requestMatchers("/hello/**").permitAll()
                .requestMatchers(("/smsLogin")).permitAll()
                .requestMatchers("/oauth2/**").permitAll()
                .anyRequest().authenticated()
        );
        http.csrf(AbstractHttpConfigurer::disable);

        http.addFilterBefore(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 用户名密码登录
        http.formLogin((fromLogin) -> fromLogin
//                .loginPage(LOGIN_PAGE)
                .loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .permitAll());
        // todo: 可以考虑升级为自定义登录接口，禁用默认的formLogin
//        http.formLogin(AbstractHttpConfigurer::disable);
        // 三方oauth2登录
        http.oauth2Login(oauth2Login -> oauth2Login
                .successHandler(federatedLoginSuccessHandler)
                .permitAll());
        return http.build();
    }


    @Bean
    public SmsAuthenticationFilter smsAuthenticationFilter(DBUserDetailsManager dbUserDetailsManager,
                                                           StringRedisTemplate stringBoundValueOperations,
                                                           LoginSuccessHandler loginSuccessHandler) {
        SmsAuthenticationFilter smsAuthenticationFilter = new SmsAuthenticationFilter();
        smsAuthenticationFilter.setAuthenticationManager(new ProviderManager(new SmsAuthenticationProvider(dbUserDetailsManager, stringBoundValueOperations)));
        smsAuthenticationFilter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        smsAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        return smsAuthenticationFilter;
    }

    // 用于检索用户进行身份验证的 UserDetailsService 实例。
    @Bean
    public UserDetailsService userDetailsService() {
        return new DBUserDetailsManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }



    // 用于管理客户端的 RegisteredClientRepository 实例。
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("oidc-client")
                .clientSecret("{noop}secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://home.lll44556.top/login/oauth2/code/oidc-client")
                .postLogoutRedirectUri("http://localhost:5173")
//                 不需要id token
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .build())
                // 配置token settings，用于客户端刷新token时的配置
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofSeconds(10))
                        .refreshTokenTimeToLive(Duration.ofDays(7))
                        .reuseRefreshTokens(true)
                        .build())
                .build();
        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    // 用于签署访问令牌的 com.nimbusds.jose.jwk.source.JWKSource 实例.
    // 可能是它用来签署访问令牌，所以不需要配置JWTEncoder
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    // java.security.KeyPair 的一个实例，其密钥在启动时生成，用于创建上述 JWKSource 。
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return keyPair;
    }


    // 用于解码已签名访问令牌的 JwtDecoder 实例。
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    // AuthorizationServerSettings 的一个实例，用于配置 Spring Authorization Server。
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }


    // 用于自定义生成 JWT 令牌，修改JWT的唯一扩展点
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(ExternalAccountService externalAccountService) {
        return context -> {
            // 仅更改access_token令牌
            if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                return;
            }

            Authentication principal = context.getPrincipal();

            // 本地账号密码登录时, 用户密码登录/手机号登录
            if (principal instanceof UsernamePasswordAuthenticationToken || principal instanceof SmsAuthenticationToken) {
                SecurityUser securityUser = (SecurityUser) principal.getPrincipal();
                System.out.println("本地账号登录，认证中心向网关下发token");
//                System.out.println("securityUser = " + securityUser);
                context.getClaims()
                        .claim("user_id", securityUser.getId())
                        .claim("username", securityUser.getUsername());
                        // claim 不可为空，下面字段在数据库中可能为null
//                        .claim("phone", securityUser.getPhone())
//                        .claim("email", securityUser.getEmail());
                return;
            }

            // OAuth2客户端登录时
            if (principal instanceof OAuth2AuthenticationToken oauth2AuthenticationToken) {

                System.out.println("第三方oauth2登录，认证中心向网关下发token");

                String registrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId(); // 获取客户端id
                OAuth2User oAuth2User = oauth2AuthenticationToken.getPrincipal();
                Map<String, Object> attributes = oAuth2User.getAttributes();
                ExternalAccount externalAccount = externalAccountService.getById(Long.valueOf((Integer)attributes.get("id")));

                context.getClaims()
                        .claim("user_id", externalAccount.getUserId())
                        .claim("username", attributes.get("login"));

                return;
            }

        };
    }



}
