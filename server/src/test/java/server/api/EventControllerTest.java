package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.EventRepository;
import commons.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventControllerTest {
    private EventRepository repo;
    private EventController controller;
    private Event event;
    private List<Debt> debtList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private List<Expense> expenseList = new ArrayList<>();
    private Date date = new Date(2002, 12, 24);

    @BeforeEach
    void setup(){
        repo = new TestEventRepository();
        controller = new EventController(repo);
        event = new Event("title");
        User user1 = new User("Ultimo", "English");
        userList.add(user1);
        User user2 = new User("Geertson", "bank", "Dutch");
        userList.add(user2);
        Debt debt1 = new Debt(event, user1, user2, 20.0);
        Debt debt2 = new Debt(event, user2, user1, 10.0);
        Expense expense1 = new Expense(user1, 20.0, userList, "name", date, ExpenseType.FOOD);

    }

    @Test
    void getAllTest() {
        controller.addEvent(event);
        controller.addEvent(event);
        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        eventList.add(event);
        assertEquals(2, controller.getAll().size());
    }

    @Test
    void addEventTest() {
        controller.addEvent(event);
        assertEquals(1, controller.getAll().size());

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
        Event check = eventList2.getFirst();
        assertEquals("title2", check.getTitle());
    }

}
