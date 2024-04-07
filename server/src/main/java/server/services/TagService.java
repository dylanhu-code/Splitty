package server.services;

import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.TagRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    /**
     * gets all tags from database
     * @return - list of tags
     */
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    /**
     * gets tag based on id
     * @param id - the tag id
     * @return - tag if exists
     */
    public Optional<Tag> getTagById(Long id) {
        return tagRepository.findById(id);
    }

    /**
     * creates a tag
     * @param tag - tag to create
     * @return - new tag
     */
    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    /**
     * Updates tag
     * @param id - id of tag
     * @param tag - new tag
     * @return - new updated tag
     */
    public Tag updateTag(Long id, Tag tag) {
        if (tagRepository.existsById(id)) {
            tag.setId(id);
            return tagRepository.save(tag);
        } else {
            throw new IllegalArgumentException("Tag not found with id: " + id);
        }
    }

    /**
     * deletes tag
     * @param id - id of tag to delete
     */
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    /**
     * retrieves
     * @param event
     * @return - list of tags
     */
    public List<Tag> getTagsByEvent(Long event) {
        return tagRepository.getTagsByEvent(event);
    }
}
