package org.gamed.userdatabaseservice.repository;

import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.domain.UserToFollowedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserToFollowedUserRepository extends JpaRepository<UserToFollowedUser, String> {

    @Query("SELECT u FROM UserToFollowedUser u WHERE u.user_id = :user")
    List<UserToFollowedUser> findByUser(@Param("user") User user);

    @Query("SELECT u.followed_id FROM UserToFollowedUser u WHERE u.user_id = :follower")
    List<String> findFollowed(@Param("follower") String follower);

    @Query("SELECT u.user_id FROM UserToFollowedUser u WHERE u.followed_id = :followedId")
    List<String> findFollowers(@Param("followedId") String followedId);

    @Query("SELECT u FROM UserToFollowedUser u WHERE u.user_id = :userId")
    List<UserToFollowedUser> findByUserId(@Param("userId") String userId);

    @Query("SELECT u FROM UserToFollowedUser u WHERE u.followed_id = :followedId")
    List<UserToFollowedUser> findByFollowedUserId(@Param("followedId") String followedId);

    @Query("SELECT u FROM UserToFollowedUser u WHERE u.user_id = :userId AND u.followed_id = :followedId")
    Optional<UserToFollowedUser> findByUserIdAndFollowedUserId(@Param("userId") String userId, @Param("followedId") String followedId);

    @Query("SELECT COUNT(u) FROM UserToFollowedUser u WHERE u.user_id = :userId")
    Long countByUserId(@Param("userId") String userId);

    @Query("SELECT COUNT(u) FROM UserToFollowedUser u WHERE u.followed_id = :followedId")
    Long countByFollowedUserId(@Param("followedId") String followedId);
}

