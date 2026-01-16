package top.lll44556.lll.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.lll44556.lll.auth.mapper")
public class LLLAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(LLLAuthApplication.class, args);
    }

}
