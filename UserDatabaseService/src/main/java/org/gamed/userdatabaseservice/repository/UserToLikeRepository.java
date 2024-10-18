package org.gamed.userdatabaseservice.repository;

import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.domain.UserToLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserToLikeRepository extends JpaRepository<UserToLike, String> {
    List<UserToLike> findByUserId(String userId);

    Optional<UserToLike> findByUserAndItemId(String userId, String itemId);

    List<UserToLike> findByItemId(String itemId);

    Long countByItemId(String itemId);
}
