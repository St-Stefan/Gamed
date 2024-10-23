package org.gamed.gamelistdatabaseservice.repository;

import org.gamed.gamelistdatabaseservice.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, String> {

    @Query("SELECT g FROM Game g WHERE g.id = :id")
    Game getGameById(@Param("id") String id);

    @Query("SELECT g FROM Game g WHERE g.name = :name")
    Game getGameByName(@Param("name") String name);

    @Query("SELECT g FROM Game g WHERE g.developer = :developer")
    List<Game> getGamesByDeveloper(@Param("developer") String developer);

    @Query("SELECT g FROM Game g WHERE g.release_date = :releaseDate")
    List<Game> getGamesByReleaseDate(@Param("releaseDate") String releaseDate);

    @Query("SELECT g FROM Game g WHERE g.platforms LIKE %:platform%")
    List<Game> getGamesByPlatform(@Param("platform") String platform);

    @Query("DELETE FROM Game g WHERE g.id = :id")
    void deleteGameById(@Param("id") String id);

    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM Game g WHERE g.name = :name")
    boolean existsByName(@Param("name") String name);
}