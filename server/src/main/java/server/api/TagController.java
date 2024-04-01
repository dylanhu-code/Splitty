package server.api;

import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.services.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * Gets all tags from database
     * @return - a list of tags
     */
    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    /**
     * Gets the tag based on id
     * @param id - id of tag
     * @return - tag with the specific id
     */
    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable Long id) {
        return tagService.getTagById(id).orElse(null);
    }

    /**
     * creates a tag
     * @param tag - new tag
     * @return - the tag created
     */

    @PostMapping
    public Tag createTag(@RequestBody Tag tag) {
        return tagService.createTag(tag);
    }

    /**
     * Updates the tag
     * @param id - id of tag to update
     * @param tag - new tag
     * @return - new tag
     */
    @PutMapping("/{id}")
    public Tag updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        return tagService.updateTag(id, tag);
    }

    /**
     * Deletes tag from database
     * @param id - id of tag to delete
     */
    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
    }
}

