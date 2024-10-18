package org.gamed.userdatabaseservice.repository;

import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.domain.UserToFollowedList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserToFollowedListRepository extends JpaRepository<UserToFollowedList, String> {
    List<UserToFollowedList> findByUserId(String user_id);

    @Query("SELECT u FROM UserToFollowedList u WHERE u.user = :user AND u.list_id = :list_id")
    Optional<UserToFollowedList> findByUserAndListId(User user, String list_id);

    List<UserToFollowedList> findByListId(String list_id);

    List<UserToFollowedList> findByTimeAfter(LocalDateTime time);

    Long countByListId(String list_id);
}