package server.api;

import commons.Event;
import commons.Quote;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private EventRepository repository;

    public EventController(EventRepository repository) {
        this.repository = repository;
    }
    @GetMapping(path = { "", "/" })
    public List<Event> getAll() {
        return repository.findAll();
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Event> addEvent(@RequestBody Event event) {
        try {
            if (event.getTitle().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            event.setCreationdate(LocalDateTime.now());
            event.setLastActivity(LocalDateTime.now());


            Event createdEvent = repository.save(event);
            return ResponseEntity.ok(createdEvent);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
