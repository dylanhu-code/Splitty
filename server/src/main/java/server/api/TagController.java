package server.api;

import com.google.inject.Inject;
import commons.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private TagService tagService;

    /**
     * Constructor
     * @param service -  tag service
     */
    @Inject
    public TagController(TagService service) {
        this.tagService = service;
    }

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
     * @return - response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id) {
        try {
            if (tagService.getTagById(id) == null) {
                return ResponseEntity.notFound().build();
            }
            tagService.deleteTag(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Gets the tags from a particualr event
     * @param eventId - particular event id
     * @return - corresponding list of tags
     */
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Tag>> getTagsByEvent(@PathVariable Long eventId) {
        try {
            List<Tag> tags = tagService.getTagsByEvent(eventId);
            return ResponseEntity.ok(tags);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }

}

