package commons;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static commons.ExpenseType.FOOD;
import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    private Event testEvent = new Event("TestEvent");
    private Event testEvent2 = new Event("TestEvent2");
    private User testUser1 = new User("ultimo","bank account", "dutch");
    private User testUser2 = new User("ultimo", "english");

    private Date testDate = new Date(124, Calendar.JUNE,5);
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
        List<Debt> testList = new ArrayList<>();
        testList.add(testDebt);
        assertEquals(testList, testEvent.getDebts());
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
        Expense testExpense = new Expense(testUser1, 10.0, userList, "name", testDate, FOOD);
        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(testExpense);
        testEvent.addExpense(testExpense);
        assertEquals(expenseList, testEvent.getExpenses());

    }

    @Test
    void removeExpense() {
        ArrayList<User> userList = new ArrayList<>();
        Expense testExpense = new Expense(testUser1, 10.0, userList, "name", testDate, FOOD);
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
        assertNotEquals(0, testEvent.getId());
        assertNotEquals(0, testEvent2.getId());
        assertNotEquals(testEvent.getId(), testEvent2.getId());
        
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
        ArrayList<Debt> testDebtList = new ArrayList<>();
        testDebtList.add(testDebt);
        testEvent.addDebt(testDebt);
        assertEquals(testDebtList, testEvent.getDebts());
    } //Done

    @Test
    void testGetStaticId() {
        int initialStaticId = Event.getStaticId();
        Event event1 = new Event("Event 1");
        Event event2 = new Event("Event 2");
        assertEquals(initialStaticId + 2, Event.getStaticId());
    }

    @Test
    void getExpenses() {
        List<User> testList = new ArrayList<>();
        testList.add(testUser1);
        testList.add(testUser2);
        Expense testExpense = new Expense(testUser1, 15.0,
            testList, "bread", testDate, FOOD);
        testEvent.addExpense(testExpense);
        ArrayList<Expense> testExpenseList = new ArrayList<>();
        testExpenseList.add(testExpense);
        assertEquals(testExpenseList, testEvent.getExpenses());
    }//Done

    @Test
    void setTitle() {
        Event coolTestEvent = new Event("Nice holiday");
        coolTestEvent.setTitle("Stupid holiday");
        assertEquals("Stupid holiday", coolTestEvent.getTitle());
    }//Done

    @Test
    void testEquals() {
        assertNotEquals(testEvent, testEvent2);
        assertEquals(testEvent,testEvent);
    }//Done

    @Test
    void testHashCode() {
        assertEquals(testEvent.hashCode(), testEvent.hashCode());
        assertNotEquals(testEvent2.hashCode(), testEvent.hashCode());
    }//Done
}
