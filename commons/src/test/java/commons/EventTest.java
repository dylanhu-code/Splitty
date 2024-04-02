package commons;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    private Event testEvent = new Event("TestEvent");
    private Event testEvent2 = new Event("TestEvent2");
    private Participant testUser1 = new Participant("ultimo",null, null, null);
    private Participant testUser2 = new Participant("ultimo", null, null, null);

    private Date testDate = new Date(124, Calendar.JUNE,5);
    private Debt testDebt = new Debt( testUser1, testUser2, 20.0);

    @Test
    void addParticipant() {
        testEvent.addParticipant(testUser1);
        List<Participant> testList = new ArrayList<>();
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
        ArrayList<Participant> userList = new ArrayList<>();
        userList.add(testUser1);
        userList.add(testUser2);
        Expense testExpense = new Expense(testUser1, 10.0, userList, "name", testDate, new Tag());
        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(testExpense);
        testEvent.addExpense(testExpense);
        assertEquals(expenseList, testEvent.getExpenses());

    }

    @Test
    void removeExpense() {
        ArrayList<Participant> userList = new ArrayList<>();
        Expense testExpense = new Expense(testUser1, 10.0, userList, "name", testDate, new Tag());
        testEvent.addExpense(testExpense);
        testEvent.removeExpense(testExpense);
        assertEquals(0, testEvent.getExpenses().size());
    }

    @Test
    void getTitle() {
        assertEquals("TestEvent", testEvent.getTitle());
    }

    @Test
    void getParticipants() {
        testEvent.addParticipant(testUser1);
        testEvent.addParticipant(testUser2);
        List<Participant> testList = new ArrayList<>();
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
    void getExpenses() {
        List<Participant> testList = new ArrayList<>();
        testList.add(testUser1);
        testList.add(testUser2);
        Expense testExpense = new Expense(testUser1, 15.0,
            testList, "bread", testDate, new Tag());
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

    @Test
    void setTestLastAcitity() {
        LocalDateTime expectedLastActivity = LocalDateTime.of(2022, 1, 1, 10, 30);
        testEvent.setLastActivity(expectedLastActivity);
        assertEquals(expectedLastActivity, testEvent.getLastActivity());
    }
    @Test
    void setTestCreationDate() {
        LocalDateTime expected = LocalDateTime.of(2021, 1, 1, 10, 30);
        testEvent.setCreationDate(expected);
        assertEquals(expected, testEvent.getCreationDate());
    }

    @Test
    void generateDebtsTest() {
        Event event = new Event("Test");
        Participant user1 = new Participant("Alice", null, null, null);
        Participant user2 = new Participant("bob", null, null, null);
        Participant user3 = new Participant("Charlie", null, null, null);

        event.addParticipant(user1);
        event.addParticipant(user2);
        event.addParticipant(user3);

        List<Participant> b1 = List.of(user2, user3);
        Expense e1 = new Expense(user1,100.0, b1, "expense1", new Date(2029, 2,2), new Tag());
        List<Participant> b2 = List.of(user1, user3);
        Expense e2 = new Expense(user2,50.0, b2, "expense2", new Date(2029, 2,2), new Tag());
        List<Participant> b3 = List.of(user1, user2);
        Expense e3 = new Expense(user3,30.0, b3, "expense3", new Date(2029, 2,2), new Tag());

        event.addExpense(e1);
        event.addExpense(e2);
        event.addExpense(e3);

        List<Debt> debts = event.generateDebts();

        assertEquals(2, debts.size());

        for (Debt d: debts) {
            assertTrue(d.getAmount() > 0);
            assertFalse(d.getUser1().equals(d.getUser2()));
            assertFalse(d.isSettled());
        }

        Debt expectedDebt1 = new Debt( user2, user1, 15.0);
        Debt expectedDebt2 = new Debt( user3, user1, 45.0);
        List<Debt> expectedDebts = List.of(expectedDebt2, expectedDebt1);
        assertEquals(expectedDebts, debts);
    }

    @Test
    void testInviteCodeGeneratorAndGetter() {
        assertNull(testEvent.getInviteCode());
        testEvent.inviteCodeGeneratorAndSetter();
        String inviteCode = testEvent.getInviteCode();
        assertNotNull(inviteCode);
        assertEquals(8,inviteCode.length());
        String characters = "abcdefghijklmnopqrstuvwxyz" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < inviteCode.length(); i++) {
            char c = inviteCode.charAt(i);
            assertTrue(characters.indexOf(c) != -1);
        }

    }

    @Test
    void testToString() {
        String expected = "Event {" +
                "\n  Title: 'TestEvent'" +
                "\n  Id: 0" +
                "\n  Participants: []" +
                "\n  Debts: []" +
                "\n  Expenses: []" +
                "\n  Created: null" +
                "\n  Last Activity: null" +
                "\n  inviteCode: 'null'" +
                "\n}";
        assertEquals(expected, testEvent.toString());
    }
}
