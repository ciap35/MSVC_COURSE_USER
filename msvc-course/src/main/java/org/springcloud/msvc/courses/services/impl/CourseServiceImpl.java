package org.springcloud.msvc.courses.services.impl;

import org.springcloud.msvc.courses.entity.Course;
import org.springcloud.msvc.courses.entity.CourseUser;
import org.springcloud.msvc.courses.models.User;
import org.springcloud.msvc.courses.provider.UserClient;
import org.springcloud.msvc.courses.repositories.CourseRepository;
import org.springcloud.msvc.courses.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseRepository repository;

    @Autowired
    UserClient userClient;

    @Override
    @Transactional(readOnly=true)
    public List<Course> findAll() {
        //return (List<Course>) repository.findAll();
        //List<Course> courseLst = (List<Course>) repository.findAll();
        //courseLst.parallelStream().map(x->x.setCourseUsers(repository.findAllById()))
        return (List<Course>) repository.findAll();
    }

    @Override
    @Transactional(readOnly=true)
    public Optional<Course> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Course save(Course course) {
        return repository.save(course);
    }

    @Override
    @Transactional
    public void delete(Long id) {
      /*Optional<Course> course = findById(id);
      if(course.isPresent()){
          List<User> userList =  course.get().getUsers();
          if(!userList.isEmpty()){
              userList.parallelStream().map(User::getId).forEach(x->{ userClient.delete(x); });
          }
      }*/
        repository.deleteById(id);

    }

    @Override
    public Optional<User> assignUser(User user, Long courseId) {
        Optional<Course> course = repository.findById(courseId);
        if(course.isPresent()){
            User coreUser = userClient.get(user.getId());
            Course courseObj = course.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(coreUser.getId());
            courseObj.addCourseUser(courseUser);
            repository.save(courseObj);
            return Optional.of(coreUser);
        }
        return Optional.empty();
    }



    @Override
    @Transactional
    public Optional<User> createUser(User user, Long courseId) {
        Optional<Course> course = repository.findById(courseId);
        if(course.isPresent()){
            User coreUser = userClient.create(user);
            Course courseObj = course.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(coreUser.getId());
            courseObj.addCourseUser(courseUser);
            repository.save(courseObj);
            return Optional.of(coreUser);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> unnassignUser(User user, Long courseId) {
        Optional<Course> course = repository.findById(courseId);
        if(course.isPresent()){
            User coreUser = userClient.get(user.getId());
            Course courseObj = course.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(coreUser.getId());
            courseObj.removeCourseUser(courseUser);
            repository.save(courseObj);
            return Optional.of(coreUser);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void unnassignUser(Long userId) {
        repository.deleteByUserId(userId);
    }

    @Override
    public Optional<Course> byUserId(Long id,String token) {
        Optional<Course> course = repository.findById(id);
        if(course.isPresent()){
            if(!course.get().getCourseUsers().isEmpty()){
                List<Long> userIdLst = course.get().getCourseUsers()
                        .stream()
                        .map(x->x.getUserId())
                        .collect(Collectors.toList());
                List<User> userList = userClient.findAllByIds(userIdLst,token);
                course.get().setUsers(userList);
                return course;
            }
        }
        return Optional.empty();
    }
}
