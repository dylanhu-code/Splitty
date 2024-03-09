package server.api;

import commons.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Timo - i've implemented this class quickly just to make sure the database connecction
 * worked and i was able to add the event
 * you will probably have to change the methods i implemented anyways, and change add
 * majority of the functionality to the EventService class
 */

@RestController
@RequestMapping("/api/events")
public class EventController {
    private EventRepository repository;

    /**
     * Constructor for event controller
     * @param repository - repository (database) for the event
     */
    public EventController(EventRepository repository) {
        this.repository = repository;
    }

    /**
     * Get method for the events in the database
     * @return - all events in database
     */
    @GetMapping(path = { "", "/" })
    public List<Event> getAll() {
        return repository.findAll();
    }

    /**
     * Post method - adds an event to database
     * @param event - event to add
     * @return - the event added
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Event> addEvent(@RequestBody Event event) {
        try {
            if (event.getTitle().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            event.setCreationdate(LocalDateTime.now());
            event.setLastActivity(LocalDateTime.now());
            event.inviteCodeGeneratorAndSetter();
            Event createdEvent = repository.save(event);
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
            repository.deleteById(id);
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
            if (id < 0 || !repository.existsById(id)) {
                return ResponseEntity.badRequest().build();
            }
            if (newEvent.getTitle() == null) {
                return ResponseEntity.badRequest().build();
            }

            newEvent.setEventId(id);
            newEvent.setLastActivity(LocalDateTime.now());
            Event updated = repository.save(newEvent);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
