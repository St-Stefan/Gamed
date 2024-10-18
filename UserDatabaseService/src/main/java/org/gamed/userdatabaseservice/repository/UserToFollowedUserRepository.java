package org.gamed.userdatabaseservice.repository;

import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.domain.UserToFollowedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserToFollowedUserRepository extends JpaRepository<UserToFollowedUser, String> {
    List<UserToFollowedUser> findByUser(User user);

    List<User> findFollowed(User follower);

    List<User> findFollowers(User followed);

    List<UserToFollowedUser> findByUserId(String userId);

    List<UserToFollowedUser> findByFollowedUserId(String followedUserId);

    Optional<UserToFollowedUser> findByUserIdAndFollowedUserId(String userId, String followedUserId);

    Long countByUserId(String userId);

    Long countByFollowedUserId(String followedUserId);
}