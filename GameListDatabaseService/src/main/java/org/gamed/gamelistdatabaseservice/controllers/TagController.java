package org.gamed.gamelistdatabaseservice.controllers;

import org.gamed.gamelistdatabaseservice.domain.Tag;
import org.gamed.gamelistdatabaseservice.models.CreateAndUpdateTagRequestModel;
import org.gamed.gamelistdatabaseservice.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for managing the Tag entity.
 */
@RestController
@RequestMapping("/tags")
@CrossOrigin(origins = "http://localhost:5173")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Creates a new Tag and stores it in the database.
     *
     * @param request the data the caller must provide to create a tag
     * @return the ID of the newly created tag
     */
    @PostMapping("/create")
    public ResponseEntity<String> createTag(@RequestBody CreateAndUpdateTagRequestModel request) {
        try {
            Tag newTag = tagService.createTag(request.getName());
            return ResponseEntity.ok(newTag.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get tag details by tag ID.
     *
     * @param tagId the ID of the tag
     * @return the Tag object if found
     */
    @GetMapping("/{tagId}")
    public ResponseEntity<Tag> getTag(@PathVariable String tagId) {
        try {
            Tag tag = tagService.getTag(tagId);
            return ResponseEntity.ok(tag);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Endpoint to get a list of all tags.
     *
     * @return a list of all Tag objects
     */
    @GetMapping("/all")
    public ResponseEntity<List<Tag>> getAllTags() {
        try {
            List<Tag> tags = tagService.getAllTagsSortedByTimeCreated();
            return ResponseEntity.ok(tags);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to delete a tag by ID.
     *
     * @param tagId the ID of the tag to be deleted
     * @return HTTP status OK if the operation succeeds
     */
    @DeleteMapping("/delete/{tagId}")
    public ResponseEntity<String> deleteTag(@PathVariable String tagId) {
        try {
            tagService.deleteTag(tagId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
