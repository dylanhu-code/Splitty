package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.EventService;
import commons.Event;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class JsonBackupControllerTest {

    @Mock
    private EventService eventService;

    private JsonBackupController jsonBackupController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jsonBackupController = new JsonBackupController(eventService);
    }

    @Test
    public void testCreateBackupSingleEvent() throws Exception {
        Event event = new Event("Test Event");
        when(eventService.findEvent(anyLong())).thenReturn(event);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String expectedResponse = objectMapper.writeValueAsString(event);

        ResponseEntity<String> response = jsonBackupController.createBackup(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testCreateBackupMultipleEvents() throws Exception {
        Event event1 = new Event("Test Event 1");
        Event event2 = new Event("Test Event 2");
        when(eventService.findEvent(anyLong())).thenReturn(event1).thenReturn(event2);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String expectedResponse = objectMapper.writeValueAsString(Arrays.asList(event1, event2));

        ResponseEntity<byte[]> response = jsonBackupController.createBackup(Arrays.asList(1L, 2L));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, new String(response.getBody()));
    }
}