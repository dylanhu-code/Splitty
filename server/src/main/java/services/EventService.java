package services;

import commons.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.database.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private EventRepository repository;

    public EventService(EventRepository repository){
        this.repository = repository;
    }

    public List<Event> getAllEvents(){
        return repository.findAll();
    }


    public void deleteEvent(long id) {

    }

    public Event updateEvent(long id, Event newEvent) {
        if (id < 0 || !repository.existsById(id) || newEvent.getTitle() == null) {
            throw new IllegalArgumentException();
        }

        newEvent.setEventId(id);
        newEvent.setLastActivity(LocalDateTime.now());
        return newEvent;
    }


}
