package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {
    private User payor;
    private double amount;
    private List<User> benificiaries;
    private String expenseName;
    private Date date;
    private ExpenseType type;
    private Expense e;
    @BeforeEach
    void setup() {
        payor = new User("mireia", "english");
        amount = 20.00;
        benificiaries = List.of(new User("jake", "english"), new User("mair", "dutch"));
        expenseName = "Taxi";
        date = new Date(2022, 3, 1);
        type = ExpenseType.TRANSPORTATION;
        e = new Expense(payor, amount, benificiaries, expenseName, date, type);
    }

    @Test
    void getPayor() {
        User expected = new User("mireia", "english");
        assertEquals(expected, e.getPayor());
    }

    @Test
    void getAmount() {
        assertEquals(20, e.getAmount());
    }

    @Test
    void getBeneficiaries() {
        List<User> expected = List.of(new User("jake", "english"), new User("mair", "dutch"));
        assertEquals(expected, e.getBeneficiaries());
    }

    @Test
    void getExpenseName() {
        assertEquals("Taxi", e.getExpenseName());
    }

    @Test
    void getDate() {
        Date expectedDate = new Date(2022, 3, 1);
        assertEquals(expectedDate, e.getDate());
    }

    @Test
    void getType() {
        assertEquals(ExpenseType.TRANSPORTATION,e.getType());
    }

    @Test
    void setPayor() {
        User newPayor = new User("jason", "english");
        assertNotEquals(newPayor, e.getPayor());
        e.setPayor(newPayor);
        assertEquals(newPayor, e.getPayor());
    }

    @Test
    void setAmount() {
        double newAmount = 30.05;
        assertNotEquals(newAmount, e.getAmount());
        e.setAmount(newAmount);
        assertEquals(newAmount, e.getAmount());
    }

    @Test
    void setBeneficiaries() {
        List<User> newBenificiaries = List.of(new User("bob", "dutch"));
        assertNotEquals(newBenificiaries , e.getBeneficiaries());
        e.setBeneficiaries(newBenificiaries);
        assertEquals(newBenificiaries, e.getBeneficiaries());
    }

    @Test
    void setExpenseName() {
        String newName = "Bus Ticket";
        assertNotEquals(newName, e.getExpenseName());
        e.setExpenseName(newName);
        assertEquals(newName, e.getExpenseName());
    }

    @Test
    void setDate() {
        Date newDate = new Date(2024, 1, 1);
        assertNotEquals(newDate, e.getDate());
        e.setDate(newDate);
        assertEquals(newDate, e.getDate());
    }

    @Test
    void setType() {
        ExpenseType newType = ExpenseType.FOOD;
        assertNotEquals(newType, e.getType());
        e.setType(newType);
        assertEquals(newType, e.getType());
    }

    @Test
    void testEquals() {
        User user1 = new User("mireia", "english");
        double amount = 20.00;
        List<User> beneficiaries = List.of(new User("jake", "english"), new User("mair", "dutch"));
        String expenseName = "Taxi";
        Date date = new Date(2022, 3, 1);
        ExpenseType type = ExpenseType.TRANSPORTATION;

        Expense newExpense = new Expense(user1, amount, beneficiaries, expenseName, date, type);

        assertEquals(e, newExpense);
    }

    @Test
    void testHashCode() {
        User user2 = new User("mireia", "english");
        double amount = 20.00;
        List<User> beneficiaries = List.of(new User("jake", "english"), new User("mair", "dutch"));
        String expenseName = "Taxi";
        Date date = new Date(2022, 3, 1);
        ExpenseType type = ExpenseType.TRANSPORTATION;

        Expense newExpense = new Expense(user2, amount, beneficiaries, expenseName, date, type);

        assertEquals(e.hashCode(), newExpense.hashCode());
    }

    @Test
    void testNotEquals() {
        User user1 = new User("toshko", "english");
        double amount = 20.00;
        List<User> beneficiaries = List.of(new User("john", "english"), new User("mair", "dutch"));
        String expenseName = "Taxi";
        Date date = new Date(2022, 3, 1);
        ExpenseType type = ExpenseType.TRANSPORTATION;

        Expense newExpense = new Expense(user1, amount, beneficiaries, expenseName, date, type);

        assertNotEquals(e, newExpense);
    }

    @Test
    void testNotEqualsHashCode() {
        User user2 = new User("gosko", "english");
        double amount = 20.00;
        List<User> beneficiaries = List.of(new User("finn", "english"), new User("mair", "dutch"));
        String expenseName = "Taxi";
        Date date = new Date(2022, 3, 1);
        ExpenseType type = ExpenseType.TRANSPORTATION;

        Expense newExpense = new Expense(user2, amount, beneficiaries, expenseName, date, type);

        assertNotEquals(e.hashCode(), newExpense.hashCode());
    }
}