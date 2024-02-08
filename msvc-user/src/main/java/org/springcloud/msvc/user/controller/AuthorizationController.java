package org.springcloud.msvc.user.controller;

import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springcloud.msvc.user.entity.User;
import org.springcloud.msvc.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
//@RequestMapping("/user/auth")
public class AuthorizationController {
    @Autowired
    private UserService service;

    private Logger log = LoggerFactory.getLogger(AuthorizationController.class);
    @GetMapping("/authorized")
    public Map<String,Object> authorized(@RequestParam(name="code") String code){
    return Collections.singletonMap("code",code);
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginByEmail(@RequestParam String email) {
        Optional<User> user = service.findByEmail(email);
        if (user.isPresent()) {
            log.info("User is present");
            return ResponseEntity.ok(user.get());
        }
        log.info("User not present: "+email);
        return ResponseEntity.notFound().build();
    }
}
