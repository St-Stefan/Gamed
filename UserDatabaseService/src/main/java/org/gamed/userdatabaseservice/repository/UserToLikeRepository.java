package org.gamed.userdatabaseservice.repository;

import org.gamed.userdatabaseservice.domain.UserToLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserToLikeRepository extends JpaRepository<UserToLike, String> {

    @Query("SELECT u FROM UserToLike u WHERE u.user.id = :userId")
    List<UserToLike> findByUserId(@Param("userId") String userId);

    @Query("SELECT u FROM UserToLike u WHERE u.user.id = :userId AND u.item_id = :itemId")
    Optional<UserToLike> findByUserAndItemId(@Param("userId") String userId, @Param("itemId") String itemId);

    @Query("SELECT u FROM UserToLike u WHERE u.item_id = :itemId")
    List<UserToLike> findByItemId(@Param("itemId") String itemId);

    @Query("SELECT COUNT(u) FROM UserToLike u WHERE u.item_id = :itemId")
    Long countByItemId(@Param("itemId") String itemId);
}
