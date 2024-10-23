package org.gamed.gamelistdatabaseservice.repository;

import org.gamed.gamelistdatabaseservice.domain.GameList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameListRepository extends JpaRepository<GameList, String> {

    @Query("SELECT l FROM GameList l WHERE l.id = :id")
    GameList getListById(@Param("id") String id);

    @Query("SELECT l FROM GameList l WHERE l.user_id = :userId")
    List<GameList> getListsByUserId(@Param("userId") String userId);

    @Query("SELECT l FROM GameList l WHERE l.name = :name")
    List<GameList> getListsByName(@Param("name") String name);

    @Query("SELECT l FROM GameList l WHERE l.user_id = :userId AND l.name = :name")
    GameList getListByUserIdAndName(@Param("userId") String userId, @Param("name") String name);

    @Query("DELETE FROM GameList l WHERE l.id = :id")
    void deleteListById(@Param("id") String id);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM GameList l WHERE l.user_id = :userId AND l.name = :name")
    boolean existsByUserIdAndName(@Param("userId") String userId, @Param("name") String name);
}