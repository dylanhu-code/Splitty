package server.api;

import com.google.inject.Inject;
import commons.Event;
import commons.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.EventService;

import java.util.List;


@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService service;


    /**
     * Constructor with the event service
     * @param service - the event service
     */
    @Inject
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

    /**
     * Add a participant to an Event
     * @param id
     * @param participant
     * @return Ok message with updated event or errorcode
     */
    @PutMapping(path = {"/addParticipant/{id}"})
    public ResponseEntity<Event> addParticipant(@PathVariable long id,
                                                @RequestBody User participant) {
        try {
            if(service.findEvent(id) == null){
                return ResponseEntity.badRequest().build();
            }
            Event updatedEvent = service.findEvent(id);
            updatedEvent.addParticipant(participant);
            service.updateEvent(id, updatedEvent);
            return ResponseEntity.ok(updatedEvent);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * removes one single participant
     * @param id
     * @param participant
     * @return Ok message with updated event or errorcode
     */
    @DeleteMapping(path = {"/removeParticipant/{id}"})
    public ResponseEntity<Event> removeParticipant(@PathVariable long id,
                                                   @RequestBody User participant){
        try {
            if(service.findEvent(id) == null){
                return ResponseEntity.badRequest().build();
            }
            Event updatedEvent = service.findEvent(id);
            updatedEvent.removeParticipant(participant);
            service.updateEvent(id, updatedEvent);
            return ResponseEntity.ok(updatedEvent);
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


}
