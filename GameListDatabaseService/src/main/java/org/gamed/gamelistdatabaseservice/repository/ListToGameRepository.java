package org.gamed.gamelistdatabaseservice.repository;

import org.gamed.gamelistdatabaseservice.domain.ListToGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ListToGameRepository extends JpaRepository<ListToGame, String> {

    @Query("SELECT ltg FROM ListToGame ltg WHERE ltg.id = :id")
    ListToGame getListToGameById(@Param("id") String id);

    @Query("SELECT ltg FROM ListToGame ltg WHERE ltg.list.id = :listId")
    List<ListToGame> getListToGamesByListId(@Param("listId") String listId);

    @Query("SELECT ltg FROM ListToGame ltg WHERE ltg.game.id = :gameId")
    List<ListToGame> getListToGamesByGameId(@Param("gameId") String gameId);

    @Query("SELECT ltg FROM ListToGame ltg WHERE ltg.list.id = :listId AND ltg.game.id = :gameId")
    ListToGame getListToGameByListIdAndGameId(@Param("listId") String listId, @Param("gameId") String gameId);

    @Query("DELETE FROM ListToGame ltg WHERE ltg.id = :id")
    void deleteListToGameById(@Param("id") String id);

    @Query("SELECT CASE WHEN COUNT(ltg) > 0 THEN true ELSE false END FROM ListToGame ltg WHERE ltg.list.id = :listId AND ltg.game.id = :gameId")
    boolean existsByListIdAndGameId(@Param("listId") String listId, @Param("gameId") String gameId);
}