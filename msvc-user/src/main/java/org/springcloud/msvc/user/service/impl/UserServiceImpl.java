package org.springcloud.msvc.user.service.impl;

import org.springcloud.msvc.user.entity.User;
import org.springcloud.msvc.user.provider.CourseApiClient;
import org.springcloud.msvc.user.repositories.UserRepository;
import org.springcloud.msvc.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private CourseApiClient courseApiClient;

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return (List<User>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    @Transactional
    public User save(User usuario) {
        return repository.save(usuario);
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        repository.deleteById(userId);
        deleteUserFromCourse(userId);
    }

    @Override
    public void deleteUserFromCourse(Long userId) {
        courseApiClient.unnassignUser(userId);
    }

    @Override
    public List<User> getAllById(Iterable<Long> ids) {
        return (List<User>)repository.findAllById(ids);
    }
}
