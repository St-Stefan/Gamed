package org.gamed.userdatabaseservice.repository;

import org.gamed.userdatabaseservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    User getUserById(String id);

    List<User> getUserByName(String name);

    void deleteUserById(String id);

    boolean existsByEmail(String email);
}