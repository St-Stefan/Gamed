package org.gamed.gamelistdatabaseservice.repository;

import org.gamed.gamelistdatabaseservice.domain.GameToTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameToTagRepository extends JpaRepository<GameToTag, String> {

    @Query("SELECT gt FROM GameToTag gt WHERE gt.id = :id")
    GameToTag getGameToTagById(@Param("id") String id);

    @Query("SELECT gt FROM GameToTag gt WHERE gt.game.id = :gameId")
    List<GameToTag> getGameToTagsByGameId(@Param("gameId") String gameId);

    @Query("SELECT gt FROM GameToTag gt WHERE gt.tag.id = :tagId")
    List<GameToTag> getGameToTagsByTagId(@Param("tagId") String tagId);

    @Query("SELECT gt FROM GameToTag gt WHERE gt.game.id = :gameId AND gt.tag.id = :tagId")
    GameToTag getGameToTagByGameIdAndTagId(@Param("gameId") String gameId, @Param("tagId") String tagId);

    @Query("DELETE FROM GameToTag gt WHERE gt.id = :id")
    void deleteGameToTagById(@Param("id") String id);

    @Query("SELECT CASE WHEN COUNT(gt) > 0 THEN true ELSE false END FROM GameToTag gt WHERE gt.game.id = :gameId AND gt.tag.id = :tagId")
    boolean existsByGameIdAndTagId(@Param("gameId") String gameId, @Param("tagId") String tagId);
}