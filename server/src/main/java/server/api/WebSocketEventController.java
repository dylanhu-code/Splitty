package server.api;

import commons.Event;
import commons.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/events")
public class WebSocketEventController {

    private final EventController eventController;

    /**
     * Constructor with the event controller
     * @param eventController - the event controller
     */
    @Autowired
    public WebSocketEventController(EventController eventController) {
        this.eventController = eventController;
    }
    /**
     * Adds a participant to an Event using Websockets
     *
     * @param id          The ID of the event
     * @param participant The participant to add
     * @return The updated event
     */

    @MessageMapping("/event/join")
    @SendTo("/topic/event/update")
    public Event joinEvent(Long id, User participant) {
        return eventController.addParticipant(id, participant).getBody();
    }

    /**
     * Remove a participant from an Event using Websockets
     *
     * @param id          The ID of the event
     * @param participant The participant to remove
     * @return The updated event
     */
    @MessageMapping("/event/leave")
    @SendTo("/topic/event/update")
    public Event leaveEvent(Long id, User participant) {
        return eventController.removeParticipant(id, participant).getBody();
    }

    /**
     * Adds an event using Websockets
     *
     * @param event The event to add
     * @return The updated event
     */
    @MessageMapping("/event/add")
    @SendTo("/topic/event/update")
    public Event addEvent(Event event) {
        return eventController.addEvent(event).getBody();
    }

    /**
     * Deletes an event using Websockets
     *
     * @param id The id of the event to delete
     * @return The updated event
     */
    @MessageMapping("/event/delete")
    @SendTo("/topic/event/update")
    public ResponseEntity<String> deleteEvent(long id) {
        return eventController.deleteEvent(id);
    }

    /**
     * Updates an event using Websockets
     *
     * @param id The id of the event to delete
     * @param newEvent The updated event
     * @return The updated event
     */
    @MessageMapping("/event/update")
    @SendTo("/topic/event/update")
    public Event updateEvent(long id, Event newEvent) {
        return eventController.updateEvent(id, newEvent).getBody();
    }
}
