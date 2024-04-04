package server.api;

import com.google.inject.Inject;
import commons.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.services.EventService;

import java.util.List;


@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService service;
    private final SimpMessagingTemplate msgs;


    /**
     * Constructor with the event service
     * @param service - the event service
     * @param msgs the SimpMessagingTemplate to handle WebSocket messaging
     */
    @Inject
    public EventController(EventService service, SimpMessagingTemplate msgs) {
        this.service = service;
        this.msgs = msgs;
    }

    /**
     * Get method for the events in the database
     * @return - all events in database
     */
    @GetMapping(path = { "", "/" })
    public List<Event> getAll() {
        return service.getAllEvents();
    }
    /**
     * Get a certain event by id
     * @return - one event
     * @param id - event to get
     */
    @GetMapping(path = {"/{id}"})
    public Event getOne(@PathVariable long id) {
        return service.findEvent(id);
    }

    /**
     * Post method - adds an event to database
     * @param event - event to add
     * @return - the added event
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Event> addEvent(@RequestBody Event event) {
        try {
            Event createdEvent = service.addEvent(event);
            msgs.convertAndSend("/topic/events/create", createdEvent);
            return ResponseEntity.ok(createdEvent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete method - deletes an event by id
     * @param id - id to remove
     * @return - ok message or error message
     */
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<String> deleteEvent(@PathVariable long id) {
        try {
            Event deletedEvent = service.findEvent(id);
            msgs.convertAndSend("/topic/events/delete", deletedEvent);
            service.deleteEvent(id);
            return ResponseEntity.ok("Event with ID " + id + " deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Sends a websocket message for deleting an event in the recently viewed list.
     * @param event
     */
    @PostMapping("/sendMsg")
    public void sendDeleteMsg(@RequestBody Event event) {
        msgs.convertAndSend("/topic/events/deleteLocally", event);
    }

    /**
     * Put method - updates an event by id
     *
     * @param id       - event to update
     * @param newEvent - updated event
     * @return - ok message or error message
     */
    @PutMapping(path = {"/{id}"})
    public ResponseEntity<Event> updateEvent(@PathVariable long id, @RequestBody Event newEvent) {
        try {
            Event updated = service.updateEvent(id, newEvent);
            msgs.convertAndSend("/topic/events/update", updated);
            return ResponseEntity.ok(updated);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Orders events by creation date
     * @return - ok message or error message
     */
    @GetMapping("/orderByCreationDate")
    public ResponseEntity<List<Event>> getEventsOrderedByCreationDate() {
        try {
            List<Event> orderedEvents = service.getEventsOrderedByCreationDate();
            return ResponseEntity.ok(orderedEvents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Orders events by title
     * @return - ok message or error message
     */
    @GetMapping("/orderByTitle")
    public ResponseEntity<List<Event>> getEventsOrderedByTitle() {
        try {
            List<Event> orderedEvents = service.getEventsOrderedByTitle();
            return ResponseEntity.ok(orderedEvents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Orders events by creation date
     * @return - ok message or error message
     */
    @GetMapping("/orderByLastActivity")
    public ResponseEntity<List<Event>> getEventsOrderedByLastActivity() {
        try {
            List<Event> orderedEvents = service.getEventsOrderedByLastActivity();
            return ResponseEntity.ok(orderedEvents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * retrieves the event which has the invite code provided
     * @param inviteCode - the invite code of the wanted event
     * @return - Event corresponding to invite code, or an error message
     */
    @GetMapping("/invite/{inviteCode}")
    public ResponseEntity<?> getEventByInviteCode(@PathVariable String inviteCode) {
        try {
            Event event = service.getEventByInviteCode(inviteCode);
            if (event != null) {
                return ResponseEntity.ok(event);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Invite code does correspond to any event");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your request");
        }
    }


}
