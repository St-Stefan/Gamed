package org.gamed.gamelistdatabaseservice.repository;

import org.gamed.gamelistdatabaseservice.domain.ListToTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ListToTagRepository extends JpaRepository<ListToTag, String> {

    @Query("SELECT ltt FROM ListToTag ltt WHERE ltt.id = :id")
    ListToTag getListToTagById(@Param("id") String id);

    @Query("SELECT ltt FROM ListToTag ltt WHERE ltt.id = :listId")
    List<ListToTag> getListToTagsByListId(@Param("listId") String listId);

    @Query("SELECT ltt FROM ListToTag ltt WHERE ltt.id = :tagId")
    List<ListToTag> getListToTagsByTagId(@Param("tagId") String tagId);

    @Query("SELECT ltt FROM ListToTag ltt WHERE ltt.id = :listId AND ltt.id = :tagId")
    ListToTag getListToTagByListIdAndTagId(@Param("listId") String listId, @Param("tagId") String tagId);

    @Query("DELETE FROM ListToTag ltt WHERE ltt.id = :id")
    void deleteListToTagById(@Param("id") String id);

    @Query("SELECT CASE WHEN COUNT(ltt) > 0 THEN true ELSE false END FROM ListToTag ltt WHERE ltt.id = :listId AND ltt.id = :tagId")
    boolean existsByListIdAndTagId(@Param("listId") String listId, @Param("tagId") String tagId);
}