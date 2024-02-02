package org.springcloud.msvc.user.repositories;

import org.springcloud.msvc.user.entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //Isn't necessary since we extend from CrudRepository
public interface UserRepository extends CrudRepository<User,Long> {
     @Query("select u from User u where u.email = :email")
     Optional<User> findByEmail(String email);
     boolean existsByEmail(String email);

}
