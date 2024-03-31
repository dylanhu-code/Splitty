package server.services;

import commons.Event;

import java.util.List;

public class AdminService {

    private EventService eventService;

    /**
     * Returns all events.
     * @return list of all events
     */
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    /**
     * Deletes an event by its ID.
     * @param id of the event to be deleted
     */
    public void deleteByID(long id) {
        eventService.deleteEvent(id);
    }
}
