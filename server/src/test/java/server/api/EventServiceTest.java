package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.EventRepository;
import commons.Event;
import server.services.EventService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    private EventService eventService;

    /**
     * Checkstyle for pipeline
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        eventService = new EventService(eventRepository);
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testFindEvent() {
        Event event = new Event();
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Event result = eventService.findEvent(1L);

        assertEquals(event, result);
        verify(eventRepository, times(1)).findById(1L);
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testGetAllEvents() {
        Event event1 = new Event();
        Event event2 = new Event();
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        List<Event> result = eventService.getAllEvents();

        assertEquals(2, result.size());
        verify(eventRepository, times(1)).findAll();
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testDeleteEvent() {
        eventService.deleteEvent(1L);

        verify(eventRepository, times(1)).deleteById(1L);
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testUpdateEvent() {
        Event event = new Event();
        event.setTitle("Test Event");
        Event newEvent = new Event();
        newEvent.setTitle("New Event");
        newEvent.setExpenses(new ArrayList<>());

        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event result = eventService.updateEvent(1L, newEvent);

        assertEquals(event, result);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testAddEvent() {
        Event newEvent = new Event();
        newEvent.setTitle("New Event");
        Event result = eventService.addEvent(newEvent);

        Event expectedEvent = new Event();
        expectedEvent.setTitle("New Event");
        expectedEvent.setCreationDate(result.getCreationDate());
        expectedEvent.setInviteCode(result.getInviteCode());

        assertEquals(expectedEvent, result);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testGetEventsOrderedByCreationDate() {
        Event event1 = new Event();
        event1.setCreationDate(LocalDateTime.now().minusDays(1));
        Event event2 = new Event();
        event2.setCreationDate(LocalDateTime.now());
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        List<Event> result = eventService.getEventsOrderedByCreationDate();

        assertEquals(event1, result.get(0));
        assertEquals(event2, result.get(1));
        verify(eventRepository, times(1)).findAll();
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testGetEventByInviteCode() {
        Event event = new Event();
        when(eventRepository.findByInviteCode("code")).thenReturn(event);

        Event result = eventService.getEventByInviteCode("code");

        assertEquals(event, result);
        verify(eventRepository, times(1)).findByInviteCode("code");
    }
}