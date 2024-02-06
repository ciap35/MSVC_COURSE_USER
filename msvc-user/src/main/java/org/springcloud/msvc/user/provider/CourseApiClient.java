package org.springcloud.msvc.user.provider;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name="msvc-course",url ="localhost:8002")
//@FeignClient(name="msvc-course",url ="host.docker.internal:8002")
//@FeignClient(name="msvc-course",url ="msvc-cursos:8002")
@FeignClient(name="msvc-course")//,url ="${msvc.course.url}")  Is not necessary since we use service discovery client.
public interface CourseApiClient {
    @DeleteMapping("/course/user/{userId}/delete")
    void unnassignUser(@PathVariable Long userId);
}