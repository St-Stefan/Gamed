package org.gamed.gamelistdatabaseservice.controllers;

import org.gamed.gamelistdatabaseservice.domain.ListToTag;
import org.gamed.gamelistdatabaseservice.models.CreateAndUpdateListToTagRequestModel;
import org.gamed.gamelistdatabaseservice.service.ListToTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for managing ListToTag entities.
 */
@RestController
@RequestMapping("/listToTags")
public class ListToTagController {

    private final ListToTagService listToTagService;

    @Autowired
    public ListToTagController(ListToTagService listToTagService) {
        this.listToTagService = listToTagService;
    }

    /**
     * Creates a new ListToTag and stores it in the database.
     *
     * @param request  the data the caller must provide to create a ListToTag
     * @return the ID of the newly created ListToTag
     */
    @PostMapping("/create")
    public ResponseEntity<String> createListToTag(@RequestBody CreateAndUpdateListToTagRequestModel request) {
        try {
            ListToTag newListToTag = listToTagService.createListToTag(
                    request.getListId(),
                    request.getTagId()
            );
            return ResponseEntity.ok(newListToTag.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get ListToTag details by ListToTag ID.
     *
     * @param listToTagId the ID of the ListToTag
     * @return the ListToTag object if found
     */
    @GetMapping("/{listToTagId}")
    public ResponseEntity<ListToTag> getListToTag(@PathVariable String listToTagId) {
        try {
            ListToTag listToTag = listToTagService.getListToTagById(listToTagId);
            return ResponseEntity.ok(listToTag);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Endpoint to get a list of all ListToTags.
     *
     * @return a list of all ListToTag objects
     */
    @GetMapping("/all")
    public ResponseEntity<List<ListToTag>> getAllListToTags() {
        try {
            List<ListToTag> listToTags = listToTagService.getAllListToTags();
            return ResponseEntity.ok(listToTags);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to delete a ListToTag by ID.
     *
     * @param listToTagId the ID of the ListToTag to be deleted
     * @return HTTP status OK if the operation succeeds
     */
    @DeleteMapping("/delete/{listToTagId}")
    public ResponseEntity<String> deleteListToTag(@PathVariable String listToTagId) {
        try {
            listToTagService.deleteListToTagById(listToTagId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
