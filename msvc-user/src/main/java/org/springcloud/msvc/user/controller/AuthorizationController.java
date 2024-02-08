package org.springcloud.msvc.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
//@RequestMapping("/user/auth")
public class AuthorizationController {
    @GetMapping("/authorized")
    public Map<String,Object> authorized(@RequestParam(name="code") String code){
    return Collections.singletonMap("code",code);
    }
}
