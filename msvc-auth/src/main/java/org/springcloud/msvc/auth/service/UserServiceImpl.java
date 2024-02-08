package org.springcloud.msvc.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springcloud.msvc.auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    private WebClient.Builder webClient;

    private Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try{
           User user = webClient
                    .build()
                    .get()
                    .uri("http://msvc-user:8001/login",uri -> uri.queryParam("email",email).build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
            log.info("User Login");
            log.info("User details: "+user.getEmail()+ " "+user.getName());
           return new org.springframework.security.core.userdetails.User(
                   email,user.getPassword(),
                   true,
                   true,
                   true,
                   true,
                   Collections.singleton(
                           new SimpleGrantedAuthority("ROLE_USER")
                   )
           );
        } catch (RuntimeException e){
            String errorMsg = "Error occured during the Login, user it doesn't exist.";
            throw new UsernameNotFoundException(errorMsg);
        }
    }
}
