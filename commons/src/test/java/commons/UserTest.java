package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("user", "english");
    }

    @Test
    void getUsernameTest() {
        User user = new User("user", "english");
        assertEquals("user", user.getUsername());
    }

    @Test
    void editUsernameTest() {
        User user = new User("user", "english");
        user.editUsername("new");
        assertEquals("new", user.getUsername());
    }

    @Test
    void getEventsNullTest() {
        User user = new User("user", "english");
        assertEquals(new ArrayList<Event>(), user.getEvents());
    }

    @Test
    void addEventTest() {
        Event event1 = new Event("Event test 1");
        Event event2 = new Event("Event test 2");
        user.addEvent(event1);
        user.addEvent(event2);

        List<Event> events = user.getEvents();
        assertNotNull(events);
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
    }

    @Test
    void getEventsTest() {
        List<Event> events = user.getEvents();
        assertNotNull(events);
        assertEquals(0, events.size());

        Event event1 = new Event("Event test 1");
        Event event2 = new Event("Event test 2");
        user.addEvent(event1);
        user.addEvent(event2);

        events = user.getEvents();
        assertNotNull(events);
        assertEquals(2, events.size());

        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
    }

    @Test
    void getBankAccountTest() {
        User user = new User("user", "03664748","english");
        assertTrue(user.getBankAccount().isPresent());
        assertEquals("03664748", user.getBankAccount().get());
    }

    @Test
    void setBankAccountTest() {
        User user = new User("user", "03664748","english");
        user.setBankAccount("0745632");
        assertTrue(user.getBankAccount().isPresent());
        assertEquals("0745632", user.getBankAccount().get());
    }

    @Test
    void getLanguageTest() {
        User user = new User("user", "english");
        assertEquals("english", user.getLanguage());
    }

    @Test
    void switchLanguageTest() {
        User user = new User("user", "english");
        user.switchLanguage("dutch");
        assertEquals("dutch", user.getLanguage());
    }

    @Test
    void testNotEquals() {
        User user = new User("user", "english");
        User user2 = new User("user2", "english");
        assertNotEquals(user, user2);
    }
    @Test
    void testEquals() {
        User user = new User("user", "english");
        User user2 = new User("user", "english");
        assertEquals(user, user2);
    }

    @Test
    void testHashCodeEqual() {
        User user = new User("user", "english");
        User user2 = new User("user", "english");
        assertEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    void testHashCodeNotEqual(){
        User user = new User("user", "english");
        User user2 = new User("user", "dutch");
        assertNotEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    void expenseManagementTest() {
        Event event = new Event("Event test");

        User user1 = new User("user1", "english");
        User user2 = new User("user2", "english");
        List<User> participants = new ArrayList<>();
        participants.add(user1);
        participants.add(user2);
        Date date = new Date(2023, 6, 2);

        user.createExpense(event, 100.0, "Expense 1", participants, date, ExpenseType.DRINKS);

        assertEquals(1, event.getExpenses().size());

        Expense expense = event.getExpenses().get(0);
        user.editExpenseAmount(event, expense, 200);

        assertEquals(200, expense.getAmount());

        user.removeExpense(event, expense);
        assertEquals(0, event.getExpenses().size());
    }

    @Test
    void testEditExpenseTitle() {
        Event event = new Event("Event test");
        user.addEvent(event);

        List<User> participants = new ArrayList<>();
        participants.add(user);
        Expense expense = new Expense(
                user,
                100.0,
                participants,
                "Expense",
                null,
                ExpenseType.DRINKS);
        event.addExpense(expense);

        user.editExpenseTitle(event, expense, "Updated name");

        assertEquals("Updated name", expense.getExpenseName());
    }

    @Test
    void testEditExpenseParticipants() {
        Event event = new Event("Event test");
        user.addEvent(event);

        List<User> initialParticipants = new ArrayList<>();
        initialParticipants.add(user);
        Expense expense = new Expense(
                user,
                100.0, initialParticipants,
                "Expense",
                null,
                ExpenseType.FOOD);
        event.addExpense(expense);

        User user2 = new User("User 2", "english");
        user.editExpenseParticipants(event, expense, List.of(user, user2));

        List<User> updatedParticipants = expense.getBeneficiaries();
        assertEquals(2, updatedParticipants.size());
        assertTrue(updatedParticipants.contains(user));
        assertTrue(updatedParticipants.contains(user2));
    }

}
