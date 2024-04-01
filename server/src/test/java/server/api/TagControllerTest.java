package server.api;

import commons.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.services.TagService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {

    @Mock
    private TagService tagService;

    @InjectMocks
    private TagController tagController;

    @Test
    void getAllTags() {
        List<Tag> tags = new ArrayList<>();
        when(tagService.getAllTags()).thenReturn(tags);

        assertEquals(tags, tagController.getAllTags());
    }

    @Test
    void getTagById() {
        Long id = 1L;
        Tag tag = new Tag( "TestTag", "yellow");
        tag.setId(id);
        when(tagService.getTagById(id)).thenReturn(Optional.of(tag));

        assertEquals(tag, tagController.getTagById(id));
    }

    @Test
    void createTag() {
        Tag tag = new Tag( "TestTag", "yellow");
        tag.setId(1L);
        when(tagService.createTag(tag)).thenReturn(tag);

        assertEquals(tag, tagController.createTag(tag));
    }

    @Test
    void updateTag() {
        Long id = 1L;
        Tag tag = new Tag("TestTag2", "orange");
        tag.setId(id);
        when(tagService.updateTag(id, tag)).thenReturn(tag);

        assertEquals(tag, tagController.updateTag(id, tag));
    }

    @Test
    void deleteTag() {
        Long id = 1L;
        tagController.deleteTag(id);
        verify(tagService, times(1)).deleteTag(id);
    }
}
