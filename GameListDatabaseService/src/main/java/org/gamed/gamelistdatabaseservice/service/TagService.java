package org.gamed.gamelistdatabaseservice.service;

import org.gamed.gamelistdatabaseservice.domain.Tag;
import org.gamed.gamelistdatabaseservice.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Creates a new Tag and stores it in the database.
     *
     * @param name the name of the tag
     * @return the newly created Tag object
     * @throws IllegalArgumentException if the name is blank
     */
    public Tag createTag(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be blank.");
        }

        if (tagRepository.existsByName(name)) {
            throw new IllegalArgumentException("A tag with this name already exists.");
        }

        Tag tag = new Tag(name, LocalDateTime.of(2023,1,1,1,0,0));
        return tagRepository.save(tag);
    }

    /**
     * Gets the Tag by id from the database.
     *
     * @param id The id of the Tag to get
     * @return the Tag mapped to the id
     * @throws IllegalArgumentException when the given id does not map to any Tag
     */
    public Tag getTag(String id) throws IllegalArgumentException {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isEmpty()) {
            throw new IllegalArgumentException("Given Tag id does not correspond to any Tag. ID: " + id);
        }
        return tag.get();
    }

    /**
     * Deletes a Tag by id from the database.
     *
     * @param id The id of the Tag to delete
     * @throws IllegalArgumentException when the given id does not map to any Tag
     */
    public void deleteTag(String id) throws IllegalArgumentException {
        if (!tagRepository.existsById(id)) {
            throw new IllegalArgumentException("Given Tag id does not correspond to any Tag. ID: " + id);
        }
        tagRepository.deleteById(id);
    }

    /**
     * Retrieves all tags created within a specific time range.
     *
     * @param startTime the start of the time range
     * @param endTime   the end of the time range
     * @return a list of tags created within the given time range
     */
    public List<Tag> getTagsByTimeRange(int startTime, int endTime) {
        return tagRepository.getTagsByTimeRange(startTime, endTime);
    }

    /**
     * Retrieves all tags sorted by creation time (most recent first).
     *
     * @return a list of all tags sorted by creation time
     */
    public List<Tag> getAllTagsSortedByTimeCreated() {
        return tagRepository.getAllTagsSortedByTimeCreated();
    }

    /**
     * Retrieves a tag by its name.
     *
     * @param name the name of the tag to retrieve
     * @return the Tag with the given name
     * @throws IllegalArgumentException if no tag with the given name exists
     */
    public Tag getTagByName(String name) throws IllegalArgumentException {
        Tag tag = tagRepository.getTagByName(name);
        if (tag == null) {
            throw new IllegalArgumentException("No tag found with the name: " + name);
        }
        return tag;
    }
}