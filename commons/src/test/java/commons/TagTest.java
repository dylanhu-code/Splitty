package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {
    private Tag tag1;
    private Tag tag2;
    @BeforeEach
    void setup() {
        tag1 = new Tag("food", "green", 1L);
        tag2 = new Tag("transportation", "red", 2L);

    }

    @Test
    void getEvent() {
        assertEquals(1L, tag1.getEvent());
    }

    @Test
    void setEvent() {
        assertNotEquals(2L, tag1.getEvent());
        tag1.setEvent(2L);
        assertEquals(2L, tag1.getEvent());
    }

    @Test
    void setId() {
        tag1.setId(1L);
        assertEquals(1L, tag1.getId());
    }

    @Test
    void getName() {
        assertEquals("food", tag1.getName());
    }

    @Test
    void setName() {
        assertNotEquals("things", tag1.getName());
        tag1.setName("things");
        assertEquals("things", tag1.getName());
    }

    @Test
    void getColor() {
        assertEquals("green", tag1.getColor());
    }

    @Test
    void setColor() {
        assertNotEquals("blue", tag1.getColor());
        tag1.setColor("blue");
        assertEquals("blue", tag1.getColor());
    }

    @Test
    void testEquals() {
        Tag same = new Tag("food", "green", 1L);
        assertEquals(same, tag1);
        assertNotEquals(tag1, tag2);
    }

    @Test
    void testHashCode() {
        Tag same = new Tag("food", "green", 1L);
        assertEquals(same.hashCode(), tag1.hashCode());
    }
}