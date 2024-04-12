package client.scenes;

import client.EventStorageManager;
import client.utils.ServerUtils;
import commons.Event;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class EventStorageManagerTest {

    @Mock
    private ServerUtils serverUtils;

    private EventStorageManager eventStorageManager;


    @BeforeEach
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        eventStorageManager = new EventStorageManager(serverUtils);
        Files.deleteIfExists(Path.of("src/main/resources/client-specific-events.json"));
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Path.of("src/main/resources/client-specific-events.json"));

    }

    @Test
    public void testSaveEventIdToFile() throws IOException {

        eventStorageManager.saveEventIdToFile(1);

        List<Long> eventIds = eventStorageManager.loadEventIdsFromFile();
        assertEquals(1, eventIds.size());
        assertTrue(eventIds.contains(1L));
    }

    @Test
    public void testGetEventsFromDatabase() {
        Event e1 = new Event("thing");
        e1.setEventId(1L);
        serverUtils.addEvent(e1);
        Event e2 = new Event("Thing2");
        e2.setEventId(2L);
        serverUtils.addEvent(e2);

        when(serverUtils.getEventById(anyLong())).thenReturn(e1, e2);

        eventStorageManager.saveEventIdToFile(1L);
        eventStorageManager.saveEventIdToFile(2L);

        List<Event> events = eventStorageManager.getEventsFromDatabase();

        assertEquals(2, events.size());
        assertEquals(e1, events.get(0));
        assertEquals(e2, events.get(1));
    }

    @Test
    public void testDeleteEventFromFile() throws IOException {

        eventStorageManager.saveEventIdToFile(1L);

        List<Long> eventIds = eventStorageManager.loadEventIdsFromFile();
        assertEquals(1, eventIds.size());
        assertTrue(eventIds.contains(1L));

        eventStorageManager.deleteEventFromFile(1);
        eventIds = eventStorageManager.loadEventIdsFromFile();
       assertFalse(eventIds.contains(1L));


    }
}
