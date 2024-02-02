package org.springcloud.msvc.courses.provider;

import org.springcloud.msvc.courses.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


//@FeignClient(name="msvc-user",url = "msvc-user:8001")
@FeignClient(name="msvc-user",url = "${msvc.user.url}")
public interface UserClient {
    @GetMapping("/user/{id}")
    User get(@PathVariable Long id);
    @PostMapping("/user/")
    User create(@RequestBody User user);
    @GetMapping("/user/cursos")
    List<User> findAllByIds(@RequestParam List<Long> ids);
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id);
}
