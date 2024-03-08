package server.api;

import commons.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import services.EventService;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/events")
public class EventController {
    private EventRepository repository;
    private EventService service;

    /**
     * Constructor for event controller
     * @param repository - repository (database) for the event
     */
    public EventController(EventRepository repository) {
        this.repository = repository;
    }

    /**
     * Constructor with the event service
     * @param service - the event service
     */
    public EventController(EventService service) {
        this.service = service;
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
     * Post method - adds an event to database
     * @param event - event to add
     * @return - the event added
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Event> addEvent(@RequestBody Event event) {
        try {
//            if (event.getTitle().isEmpty()) {
//                return ResponseEntity.badRequest().build();
//            }
//            event.setCreationdate(LocalDateTime.now());
//            event.setLastActivity(LocalDateTime.now());
//            event.inviteCodeGeneratorAndSetter();
            Event createdEvent = service.addEvent(event);
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
            service.deleteEvent(id);
            return ResponseEntity.ok("Event with ID " + id + " deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

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
            return ResponseEntity.ok(updated);
        }catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/orderByCreationDate")
    public ResponseEntity<List<Event>> getEventsOrderedByCreationDate() {
        try {
            List<Event> orderedEvents = service.getEventsOrderedByCreationDate();
            return ResponseEntity.ok(orderedEvents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
