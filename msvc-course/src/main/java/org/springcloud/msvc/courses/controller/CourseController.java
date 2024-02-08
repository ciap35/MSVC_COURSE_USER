package org.springcloud.msvc.courses.controller;

import feign.FeignException;
import org.springcloud.msvc.courses.entity.Course;
import org.springcloud.msvc.courses.models.User;
import org.springcloud.msvc.courses.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService service;
    @Autowired
    private ConfigurableApplicationContext context;

    @GetMapping("/crash")
    public void crash(){
        ((ConfigurableApplicationContext)context).close();
    }

    @GetMapping
    public ResponseEntity<List<Course>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> findById(@PathVariable Long id, @RequestHeader(value="Authorization",required = true) String token){
        Optional<Course> course = service.byUserId(id,token);
        if(course.isPresent()){
            return ResponseEntity.ok(course.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Course> save(@RequestBody Course course){
        return ResponseEntity.ok(service.save(course));
    }

    @PutMapping()
    public ResponseEntity<Course> update(@RequestBody Course course){
        Optional<Course> originalCourse = service.findById(course.getId());
        if(originalCourse.isPresent()){
            originalCourse.get().setName(course.getName());
            return ResponseEntity.status(HttpStatus.OK).body(service.save(course));
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

    @PutMapping("/assign/user/{courseId}")
    public ResponseEntity<?> assignUser(@RequestBody User request, @PathVariable Long courseId){
        Optional<User> user;
        try{
            user = service.assignUser(request,courseId);
        }   catch (FeignException fex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Error","Course cannot be found"));
        }
            if(user.isPresent()){
                return ResponseEntity.status(HttpStatus.CREATED).body(user);
            }
            return ResponseEntity.notFound().build();
    }

    @PostMapping("/create/user/{courseId}")
        public ResponseEntity<?> createUser(@RequestBody User request, @PathVariable Long courseId){
            Optional<User> user;
            try{
                user = service.createUser(request,courseId);
            }   catch (FeignException fex){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Error","User cannot be created"));
            }
            if(user.isPresent()){
                return ResponseEntity.status(HttpStatus.CREATED).body(user);
            }
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/unnassign/user/{courseId}")
    public ResponseEntity<?> unassignedUser(@RequestBody User request, @PathVariable Long courseId){
        Optional<User> user;
        try{
            user = service.unnassignUser(request,courseId);
        }   catch (FeignException fex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Error","User cannot be unnasigned."));
        }
        if(user.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/user/{userId}/delete")
    public ResponseEntity<?> unassignedUser(@PathVariable Long userId){

        try{
            service.unnassignUser(userId);
        }   catch (FeignException fex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Error","User cannot be unnasigned."));
        }

        return ResponseEntity.noContent().build();
    }
}