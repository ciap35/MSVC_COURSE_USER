package org.springcloud.msvc.courses.services;

import org.springcloud.msvc.courses.entity.Course;
import org.springcloud.msvc.courses.models.User;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> findAll();
    Optional<Course> findById(Long id);
    Course save(Course course);
    void delete(Long id);
    Optional<User> assignUser(User user, Long courseId);
    Optional<User> createUser(User user, Long courseId);
    Optional<User> unnassignUser(User user, Long courseId);
    void unnassignUser(Long userId);
    Optional<Course> byUserId(Long id);
}
