package server.api;

import commons.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<String> deleteEvent(@PathVariable long id){
        try {
            repository.deleteById(id);
            return ResponseEntity.ok("Event with ID " + id + " deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    /**
     * Updates Title of an event by id.
     * Title should be in Body of request as a loose string with no "" or {} whatsoever.
     * Id should be in path
     * @param id
     * @param title
     * @return ok message or HTTP not found when the id doesn't belong to any event
     */
    @PutMapping(path = {"/setTitle/{id}"})
    public ResponseEntity<String> updateTitle(@PathVariable long id, @RequestBody String title){
        try {
            Optional<Event> event = repository.findById(id);
            if (event.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            else {
                event.get().setTitle(title);
                event.get().setLastActivity(LocalDateTime.now());
                repository.save(event.get());

            }
            return ResponseEntity.ok("Title of event " +
                    id +
                    " has been succesfully updated to be " +
                    title);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
