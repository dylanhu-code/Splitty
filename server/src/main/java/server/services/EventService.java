package server.services;

import commons.Event;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import com.google.inject.Inject;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
public class EventService {

    private final EventRepository repository;

    /**
     * Constructor for the event service
     * @param repository - the database
     */
    @Inject
    public EventService(EventRepository repository){
        this.repository = repository;
    }

    /**
     * find and return either event or null in case of id not being found
     * @param id
     * @return event or null
     */
    public Event findEvent(long id){
        if(repository.existsById(id)){
            return(repository.findById(id).get());
        }
        return null;
    }

    /**
     *  Method that returns all the events in the database
     * @return - a list containing all the events
     */
    public List<Event> getAllEvents(){
        return repository.findAll();
    }

    /**
     *  Deletes an event from the database
     * @param id - the id of the event to be deleted
     */
    public void deleteEvent(long id) {
        repository.deleteById(id);

    }

    /**
     * Updates an event
     * @param id - the id of the event to be updated
     * @param newEvent - the event containing the new information
     * @return the updated event
     */
    public Event updateEvent(long id, Event newEvent) {
        if (id < 0 || !repository.existsById(id) || newEvent.getTitle() == null) {
            throw new IllegalArgumentException();
        }
        Optional<Event> optionalEvent = repository.findById(id);
        if(optionalEvent.isPresent()){
            Event event = optionalEvent.get();
            event.setTitle(newEvent.getTitle());
            event.setDebts(newEvent.getDebts());
            event.setExpenses(newEvent.getExpenses());
            if(event.getInviteCode() == null)
                event.inviteCodeGeneratorAndSetter();
            event.setParticipants(newEvent.getParticipants());
            event.setLastActivity(LocalDateTime.now());
            repository.save(event);
            return event;
        }
        else throw new IllegalArgumentException();


    }

    /**
     * Adds an event to the database
     * @param event - the event to be added
     * @return - the added event
     */
    public Event addEvent(Event event) {
        if (event.getTitle().isEmpty()) {
            throw new IllegalArgumentException();
        }
        event.setCreationDate(LocalDateTime.now());
        event.setLastActivity(LocalDateTime.now());
        event.inviteCodeGeneratorAndSetter();
        while (repository.findByInviteCode(event.getInviteCode()) != null) {
            event.inviteCodeGeneratorAndSetter();
        }
        repository.save(event);
        return event;
    }

    /**
     * Orders the events by creation date
     * @return - the sorted list containing all the events
     */
    public List<Event> getEventsOrderedByCreationDate() {
        List<Event> events = repository.findAll();
        events.sort(Comparator.comparing(Event::getCreationDate));
        return events;
    }

    /**
     * Orders the events by last activity
     * @return - the sorted list containing all the events
     */
    public List<Event> getEventsOrderedByLastActivity() {
        List<Event> events = repository.findAll();
        events.sort(Comparator.comparing(Event::getLastActivity));
        return events;
    }

    /**
     * Orders the events by title
     * @return - the sorted list containing all the events
     */
    public List<Event> getEventsOrderedByTitle() {
        List<Event> events = repository.findAll();
        events.sort(Comparator.comparing(Event::getTitle));
        return events;
    }

    /**
     * retrives the Event based on invitecode
     * @param code - string representing invite code
     * @return - the corresponding event or null
     */
    public Event getEventByInviteCode(String code) {
        return repository.findByInviteCode(code);
    }


}
