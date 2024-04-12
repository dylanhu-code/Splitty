package server.api;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.EventRepository;
import server.services.EventService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;


class EventControllerTest {
    @Inject
    private EventRepository repo;
    @Inject
    private EventController controller;
    @Inject
    private EventService service;
    @Mock
    private SimpMessagingTemplate msgs;
    private Event event;
    private List<Debt> debtList = new ArrayList<>();
    private List<Participant> userList = new ArrayList<>();
    private List<Expense> expenseList = new ArrayList<>();
    private Date date = new Date(2002, 12, 24);

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);

        Injector injector = Guice.createInjector(new TestModule());
        injector.injectMembers(this);
        event = new Event("title");
        Participant user1 = new Participant("Ultimo","mm.@gmail.com",
                "English", null);
        userList.add(user1);
        Participant user2 = new Participant("Geertson","mm.@gmail.com",
                "bank", "Dutch");
        userList.add(user2);
        Debt debt1 = new Debt( user1, user2, 20.0);
        Debt debt2 = new Debt(user2, user1, 10.0);

        Expense expense1 = new Expense(user1, 20.0,"EUR", userList,  "name", date,
                new Tag ("food", "red", 1L));

        controller.addEvent(event);

    }

    @Test
    void getAllTest() {
        controller.addEvent(event);
        controller.addEvent(event);
        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        eventList.add(event);
        assertEquals(3, controller.getAll().size());
    }

    @Test
    void getByInviteCodeTest(){
        String inviteCode = "nonExistent";
        ResponseEntity<?> response = controller.getEventByInviteCode(inviteCode);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getOneTest(){
        long id = event.getEventId();
        assertEquals(event, controller.getOne(id));
    }

    @Test
    void orderDateTest(){
        Event one = new Event("one");
        Event two = new Event("two");
        Event three = new Event("three");

        List<Event> events = new ArrayList<>();
        controller.addEvent(two);
        controller.addEvent(three);
        controller.addEvent(one);

        events.add(event);
        events.add(two);
        events.add(three);
        events.add(one);

        assertEquals(events, controller.getEventsOrderedByCreationDate().getBody());
    }

    @Test
    void orderTitleTest(){
        Event a = new Event("a");
        Event b = new Event("b");
        Event c = new Event("c");

        List<Event> events = new ArrayList<>();
        controller.addEvent(b);
        controller.addEvent(c);
        controller.addEvent(a);
        events.add(a);
        events.add(b);
        events.add(c);
        events.add(event);

        assertEquals(events, controller.getEventsOrderedByTitle().getBody());

    }
    @Test
    void addEventTest() {
        controller.addEvent(event);
        assertEquals(2, controller.getAll().size());

    }

    @Test
    void deleteEventTest() {
        controller.addEvent(event);
        controller.deleteEvent(event.getEventId());
        assertEquals(0, controller.getAll().size());
    }

    @Test
    void updateEventTest() {
        controller.addEvent(event);
        event.setTitle("title2");
        controller.updateEvent(event.getEventId(), event);
        List<Event> eventList2 = controller.getAll();
        Event check = eventList2.get(0);
        assertEquals("title2", check.getTitle());
    }

    private class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(EventRepository.class).to(TestEventRepository.class);
            bind(EventService.class);
            bind(EventController.class);
            bind(SimpMessagingTemplate.class).toInstance(msgs);
        }
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    void sendDeleteMsgTest() {
        Event event = new Event("title");

        controller.sendDeleteMsg(event);

        verify(msgs).convertAndSend("/topic/events/deleteLocally", event);
    }
}
