package org.gamed.gamelistdatabaseservice.service;

import org.gamed.gamelistdatabaseservice.domain.ListToTag;
import org.gamed.gamelistdatabaseservice.domain.Tag;
import org.gamed.gamelistdatabaseservice.repository.GameRepository;
import org.gamed.gamelistdatabaseservice.repository.ListToTagRepository;
import org.gamed.gamelistdatabaseservice.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListToTagService {
    private final ListToTagRepository listToTagRepository;
    private final GameListService gameListService;
    private final TagService tagService;


    @Autowired
    public ListToTagService(ListToTagRepository listToTagRepository,  GameListService gameListService, TagService tagService) {
        this.listToTagRepository = listToTagRepository;
        this.gameListService = gameListService;
        this.tagService = tagService;
    }

    /**
     * Creates a new ListToTag association and stores it in the database.
     *
     * @param listId id of the list
     * @param tagId id of the list
     * @return the newly created ListToTag object
     * @throws IllegalArgumentException - when the ids are null or the relation already exists.
     */
    public ListToTag createListToTag(String listId, String tagId) {
        if(listId == null || tagId == null) {
            throw new IllegalArgumentException("Ids cannot be null");
        }
        if(existsByListIdAndTagId(listId, tagId)) {
            throw new IllegalArgumentException("Relation already exists.");
        }

        ListToTag listToTag = new ListToTag(listId, tagId);
        return listToTagRepository.save(listToTag);
    }

    /**
     * Retrieves a ListToTag by its ID.
     *
     * @param id The id of the ListToTag to get
     * @return the ListToTag object if found
     * @throws IllegalArgumentException when the given id does not map to any ListToTag
     */
    public ListToTag getListToTagById(String id) throws IllegalArgumentException {
        return Optional.ofNullable(listToTagRepository.getListToTagById(id))
                .orElseThrow(() -> new IllegalArgumentException("Given ListToTag id does not correspond to any ListToTag. ID: " + id));
    }

    /**
     * Retrieves all ListToTag associations for a specific list ID.
     *
     * @param listId The ID of the list to get associations for
     * @return a list of ListToTag objects
     */
    public List<ListToTag> getListToTagsByListId(String listId) {
        return listToTagRepository.getListToTagsByListId(listId);
    }

    /**
     * Retrieves all ListToTag associations for a specific tag ID.
     *
     * @param tagId The ID of the tag to get associations for
     * @return a list of ListToTag objects
     */
    public List<ListToTag> getListToTagsByTagId(String tagId) {
        return listToTagRepository.getListToTagsByTagId(tagId);
    }

    /**
     * Retrieves a ListToTag association by list ID and tag ID.
     *
     * @param listId The ID of the list
     * @param tagId  The ID of the tag
     * @return the ListToTag object if found
     * @throws IllegalArgumentException when the combination does not correspond to any ListToTag
     */
    public ListToTag getListToTagByListIdAndTagId(String listId, String tagId) throws IllegalArgumentException {
        return Optional.ofNullable(listToTagRepository.getListToTagByListIdAndTagId(listId, tagId))
                .orElseThrow(() -> new IllegalArgumentException("No ListToTag found for List ID: " + listId + " and Tag ID: " + tagId));
    }

    /**
     * Deletes a ListToTag by ID.
     *
     * @param id The ID of the ListToTag to delete
     * @throws IllegalArgumentException when the given id does not map to any ListToTag
     */
    public void deleteListToTagById(String id) throws IllegalArgumentException {
        if (!listToTagRepository.existsById(id)) {
            throw new IllegalArgumentException("Given ListToTag id does not correspond to any ListToTag. ID: " + id);
        }
        listToTagRepository.deleteListToTagById(id);
    }

    /**
     * Checks if a ListToTag association exists for the given list ID and tag ID.
     *
     * @param listId The ID of the list
     * @param tagId  The ID of the tag
     * @return true if the association exists, false otherwise
     */
    public boolean existsByListIdAndTagId(String listId, String tagId) {
        return listToTagRepository.existsByListIdAndTagId(listId, tagId);
    }

    /**
     * Retrieves all ListToTag associations.
     *
     * @return a list of all ListToTag objects
     */
    public List<ListToTag> getAllListToTags() {
        return listToTagRepository.findAll();
    }
}
