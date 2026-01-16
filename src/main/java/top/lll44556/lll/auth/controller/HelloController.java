package top.lll44556.lll.auth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.lll44556.lll.common.utils.R;

@RestController
@ResponseBody
public class HelloController {

    @GetMapping("/api/hello")
    public R<String> hello1() {
        return R.ok("api hello");
    }


    @GetMapping("/hello")
    public R<String> hello2(@AuthenticationPrincipal Object principal) {
        System.out.println(principal);
        return R.ok("hello");
    }

    @GetMapping("/hello/api")
    public R<String> hello3() {
        return R.ok("hello api");
    }
}
