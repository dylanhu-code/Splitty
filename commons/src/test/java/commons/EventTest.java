package commons;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    private Event testEvent = new Event("TestEvent");
    private Event testEvent2 = new Event("TestEvent2");
    private User testUser1 = new User("ultimo","bank account", "dutch");
    private User testUser2 = new User("ultimo", "english");

    //TODO
    //the testDebt and testExpense methods were made before the Debt class was added. They might work, they might not
    private Debt testDebt = new Debt(testEvent, testUser1, testUser2, 20.0);

    @Test
    void addParticipant() {
       testEvent.addParticipant(testUser1);
       List<User> testList = new ArrayList<>();
       testList.add(testUser1);
      assertEquals(testList, testEvent.getParticipants());
    }

    @Test
    void removeParticipant() {
        testEvent.addParticipant(testUser1);
        testEvent.removeParticipant(testUser1);
        assertEquals(0, testEvent.getParticipants().size());
    }

    @Test
    void addDebt() {
        testEvent.addDebt(testDebt);
        List<User> testList = new ArrayList<>();
        testList.add(testDebt);
        assertEquals(testDebt, testEvent.getDebts());
    }

    @Test
    void removeDebt() {
        testEvent.addDebt(testDebt);
        testEvent.removeDebt(testDebt);
        assertEquals(0, testEvent.getDebts().size());
    }

    @Test
    void addExpense() {
        ArrayList<User> userList = new ArrayList<>();
        userList.add(testUser1);
        userList.add(testUser2);
        Expense testExpense = new Expense(testUser1, 10.0, userList, "name", "19-10-2022", FOOD);
        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(testExpense);
        testEvent.addExpense(testExpense);
        assertEquals(expenseList, testEvent.getExpenses());

    }

    @Test
    void removeExpense() {
        Expense testExpense = new Expense(testUser1, 10.0, userList, "name", "19-10-2022", FOOD);
        testEvent.addExpense(testExpense);
        testEvent.removeExpense(testExpense);
        assertEquals(0, testEvent.getExpenses().size());
    }

    @Test
    void getTitle() {
        assertEquals("TestEvent", testEvent.getTitle());
    }

    @Test
    void getId() {
        assertNotNull(testEvent.getId());
        assertNotEquals(0, testEvent.getId());
        assertNotEquals(0, testEvent2.getId());
        assertNotEquals(testEvent.getId(), testEvent2.getId());
        
    }

    @Test
    void getStaticId() {
        assertNotNull(Event.getStaticId());
        //based on the fact I made two testevents which should have id 0 and 1 staticID should now be 2
        assertEquals(2, Event.getStaticId());
    }

    @Test
    void getParticipants() {
        testEvent.addParticipant(testUser1);
        testEvent.addParticipant(testUser2);
        List<User> testList = new ArrayList<>();
        testList.add(testUser1);
        testList.add(testUser2);
        assertEquals(testList, testEvent.getParticipants());
    }

    @Test
    void getDebts() {
        //TODO
    }

    @Test
    void getExpenses() {
        //TODO
    }

    @Test
    void setTitle() {
        //TODO
    }

    @Test
    void testEquals() {
        //TODO
    }

    @Test
    void testHashCode() {
        //TODO
    }
}
