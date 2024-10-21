package org.gamed.userdatabaseservice.repository;

import org.gamed.userdatabaseservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.id = :id")
    User getUserById(@Param("id") String id);

    @Query("SELECT u FROM User u WHERE u.name = :name")
    List<User> getUserByName(@Param("name") String name);

    @Query("DELETE FROM User u WHERE u.id = :id")
    void deleteUserById(@Param("id") String id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);
}
