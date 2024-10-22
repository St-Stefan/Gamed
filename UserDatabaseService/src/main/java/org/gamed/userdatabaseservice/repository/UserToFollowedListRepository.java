package org.gamed.userdatabaseservice.repository;

import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.domain.UserToFollowedList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserToFollowedListRepository extends JpaRepository<UserToFollowedList, String> {
    @Query("SELECT u FROM UserToFollowedList u WHERE u.user_id = :user_id")
    List<UserToFollowedList> findByUserId(@Param("user_id") String user_id);

    @Query("SELECT u FROM UserToFollowedList u WHERE u.user_id = :user AND u.list_id = :list_id")
    Optional<UserToFollowedList> findByUserAndListId(@Param("user") User user, @Param("list_id") String list_id);


    @Query("SELECT u FROM UserToFollowedList u WHERE u.list_id = :list_id")
    List<UserToFollowedList> findByListId(@Param("list_id") String list_id);

    @Query("SELECT u FROM UserToFollowedList u WHERE u.time > :time")
    List<UserToFollowedList> findByTimeAfter(@Param("time") LocalDateTime time);

    @Query("SELECT COUNT(u) FROM UserToFollowedList u WHERE u.list_id = :list_id")
    Long countByListId(@Param("list_id") String list_id);

}