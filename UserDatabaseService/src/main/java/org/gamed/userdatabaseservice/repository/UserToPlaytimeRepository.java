package org.gamed.userdatabaseservice.repository;

import org.gamed.userdatabaseservice.domain.UserToPlaytime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserToPlaytimeRepository extends JpaRepository<UserToPlaytime, String> {
    List<UserToPlaytime> findByUserId(String userId);

    List<UserToPlaytime> findByGameId(String gameId);

    Optional<UserToPlaytime> findByUserIdAndGameId(String userId, String gameId);

    Long getTotalPlaytimeForUser(String userId);

    Long getTotalPlaytimeForGame(String gameId);
}