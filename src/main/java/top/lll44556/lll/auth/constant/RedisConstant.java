package top.lll44556.lll.auth.constant;

public class RedisConstant {
    public static final String AUTH_CAPTCHA_CODE_KEY = "auth:captcha:";



    public static String getSmsCodeKey(String key) {
        return AUTH_CAPTCHA_CODE_KEY + key;
    }
}
