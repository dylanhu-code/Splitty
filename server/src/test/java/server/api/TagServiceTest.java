package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.services.TagService;
import commons.Tag;
import server.database.TagRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    /**
     * Checkstyle for pipeline
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testGetAllTags() {
        Tag tag1 = new Tag();
        Tag tag2 = new Tag();
        when(tagRepository.findAll()).thenReturn(Arrays.asList(tag1, tag2));

        List<Tag> result = tagService.getAllTags();

        assertEquals(2, result.size());
        verify(tagRepository, times(1)).findAll();
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testGetTagById() {
        Tag tag = new Tag();
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        Optional<Tag> result = tagService.getTagById(1L);

        assertEquals(tag, result.get());
        verify(tagRepository, times(1)).findById(1L);
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testCreateTag() {
        Tag tag = new Tag();
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        Tag result = tagService.createTag(new Tag());

        assertEquals(tag, result);
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testUpdateTag() {
        Tag tag = new Tag();
        when(tagRepository.existsById(1L)).thenReturn(true);
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        Tag result = tagService.updateTag(1L, new Tag());

        assertEquals(tag, result);
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testDeleteTag() {
        tagService.deleteTag(1L);

        verify(tagRepository, times(1)).deleteById(1L);
    }
}