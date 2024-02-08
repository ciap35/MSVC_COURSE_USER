package org.springcloud.msvc.user.controller;

import org.springcloud.msvc.user.entity.User;
import org.springcloud.msvc.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
//@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private ConfigurableApplicationContext context;
    @Autowired
    private Environment env;

    @GetMapping("/crash")
    public void crash(){
        ((ConfigurableApplicationContext)context).close();
    }

    @GetMapping("/environment")
    public Map<String,Object> getEnvironment(){
        Map<String,Object> body = new HashMap<>();
        body.put("podInfo",env.getProperty("MY_POD_NAME")+ " : " + env.getProperty("MY_POD_IP"));
        //Value from ConfigMap.
        body.put("env",env.getProperty("config.activeEnv"));
        return body;
    }

    @GetMapping()
    public List<User> getAll(){
       return service.getAll();
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        Optional<User> userById = service.findById(id);
        if(userById.isPresent()){
            return ResponseEntity.ok(userById);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/courses")
    public ResponseEntity<?> getUsersByCourse(@RequestParam List<Long> ids){
        return ResponseEntity.ok(service.getAllById(ids));
    }

    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result){
        if(service.existsByEmail(user.getEmail())){
            return ResponseEntity
                    .badRequest()
                    .body(
                            Collections.singletonMap("error","User already exists with that email")
                    );
        }

        ResponseEntity<Map<String, String>> errors = validateErrors(result);
        if (errors != null) return errors;
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    private ResponseEntity<Map<String, String>> validateErrors(BindingResult result) {
        if(result.hasErrors()){
            Map<String,String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error-> {
                errors.put(error.getField(),"Field:"+ error.getField()+ ": "+error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errors);
        }
        return null;
    }

    @PutMapping("/{id}")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody User newUser, @PathVariable Long id){
        Optional<User> user = service.findById(id);
        if(user.isPresent()){
            if(newUser.getEmail().equalsIgnoreCase(user.get().getEmail()) || service.findByEmail(newUser.getEmail()).isPresent()){
                return ResponseEntity
                        .badRequest()
                        .body(
                                Collections.singletonMap("error","User already exists with that email")
                        );
            }


            User modifiedUser = user.get();
            modifiedUser.setName(newUser.getName());
            modifiedUser.setLastName(newUser.getLastname());
            modifiedUser.setEmail(newUser.getEmail());
            modifiedUser.setPassword(newUser.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(service.save(modifiedUser));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<User> user = service.findById(id);
        if(user.isPresent()){
            service.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}



