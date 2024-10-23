package org.gamed.gamelistdatabaseservice.repository;

import org.gamed.gamelistdatabaseservice.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, String> {

    @Query("SELECT t FROM Tag t WHERE t.id = :id")
    Tag getTagById(@Param("id") String id);

    @Query("SELECT t FROM Tag t WHERE t.name = :name")
    Tag getTagByName(@Param("name") String name);

    @Query("SELECT t FROM Tag t WHERE t.time_created >= :startTime AND t.time_created <= :endTime")
    List<Tag> getTagsByTimeRange(@Param("startTime") int startTime, @Param("endTime") int endTime);

    @Query("SELECT t FROM Tag t ORDER BY t.time_created DESC")
    List<Tag> getAllTagsSortedByTimeCreated();

    @Query("DELETE FROM Tag t WHERE t.id = :id")
    void deleteTagById(@Param("id") String id);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Tag t WHERE t.name = :name")
    boolean existsByName(@Param("name") String name);
}