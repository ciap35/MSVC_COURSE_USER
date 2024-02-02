package org.springcloud.msvc.user.service;

import org.springcloud.msvc.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();
    Optional<User> findById(Long id); //Optional had been launch in Java 8
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    User save(User user);
    void delete(Long id);
    void deleteUserFromCourse(Long userId);
    List<User> getAllById(Iterable<Long> ids);
}
