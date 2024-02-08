package org.springcloud.msvc.courses.provider;

import org.springcloud.msvc.courses.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@FeignClient(name="msvc-user",url = "msvc-user:8001")
@FeignClient(name="msvc-user") //,url = "${msvc.user.url}") Is not necessary since we use service  discovery client.
public interface UserClient {
    @GetMapping("/{id}")
    User get(@PathVariable Long id);
    @PostMapping()
    User create(@RequestBody User user);
    @GetMapping("/courses")
    List<User> findAllByIds(@RequestParam List<Long> ids, @RequestHeader(value="Authorization",required = true) String token);
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id);
}
