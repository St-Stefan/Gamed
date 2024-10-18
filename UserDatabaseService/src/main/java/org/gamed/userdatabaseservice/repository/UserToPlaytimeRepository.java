package org.gamed.userdatabaseservice.repository;

import org.gamed.userdatabaseservice.domain.UserToPlaytime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserToPlaytimeRepository extends JpaRepository<UserToPlaytime, String> {

    @Query("SELECT u FROM UserToPlaytime u WHERE u.user.id = :userId")
    List<UserToPlaytime> findByUserId(@Param("userId") String userId);

    @Query("SELECT u FROM UserToPlaytime u WHERE u.game_id = :gameId")
    List<UserToPlaytime> findByGameId(@Param("gameId") String gameId);

    @Query("SELECT u FROM UserToPlaytime u WHERE u.user.id = :userId AND u.game_id = :gameId")
    Optional<UserToPlaytime> findByUserIdAndGameId(@Param("userId") String userId, @Param("gameId") String gameId);

    @Query("SELECT SUM(u.playtime) FROM UserToPlaytime u WHERE u.user.id = :userId")
    Long getTotalPlaytimeForUser(@Param("userId") String userId);

    @Query("SELECT SUM(u.playtime) FROM UserToPlaytime u WHERE u.game_id = :gameId")
    Long getTotalPlaytimeForGame(@Param("gameId") String gameId);
}
