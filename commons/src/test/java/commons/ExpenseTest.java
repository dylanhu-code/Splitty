package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {
    private Participant payor;
    private double amount;
    private List<Participant> benificiaries;
    private String expenseName;
    private Date date;
    private Expense e;

    @BeforeEach
    void setup() {
        payor = new Participant("mireia", null, null, null);
        amount = 20.00;
        benificiaries = List.of(new Participant("jake", null, null, null),
                new Participant("mair", null, null, null));
        expenseName = "Taxi";
        date = new Date(2022, 3, 1);

        e = new Expense(payor, amount,"EUR", benificiaries, expenseName,
                date, new Tag("food", "red", 1L));

    }

    @Test
    void getPayor() {
        Participant expected = new Participant("mireia", null, null, null);
        assertEquals(expected, e.getPayor());
    }

    @Test
    void getAmount() {
        assertEquals(20, e.getAmount());
    }

    @Test
    void getBeneficiaries() {
        List<Participant> expected = List.of(new Participant(
                        "jake", null, null, null),
                new Participant("mair", null, null, null));
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
    void getTag() {
        assertEquals(new Tag("food", "red", 1L), e.getTag());
    }

    @Test
    void setPayor() {
        Participant newPayor = new Participant(
                "jason", "english", null, null);
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
        List<Participant> newBenificiaries = List.of(
                new Participant("bob", null, null, null));
        assertNotEquals(newBenificiaries, e.getBeneficiaries());
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
        Tag newType = new Tag("other", "blue", 1L);
        assertNotEquals(newType, e.getTag());
        e.setTag(newType);
        assertEquals(newType, e.getTag());
    }

    @Test
    void testEquals() {
        Participant user1 = new Participant("mireia", null, null, null);
        double amount = 20.00;
        List<Participant> beneficiaries = List.of(new Participant(
                        "jake", null, null, null),
                new Participant("mair", null, null, null));
        String expenseName = "Taxi";
        Date date = new Date(2022, 3, 1);

        Expense newExpense = new Expense(user1, amount,"EUR", beneficiaries, expenseName,
                date,  new Tag("food", "red", 1L));

        assertEquals(e, newExpense);
    }

    @Test
    void testHashCode() {
        Participant user2 = new Participant("mireia", null, null, null);
        double amount = 20.00;
        List<Participant> beneficiaries = List.of(new Participant
                        ("jake", null, null, null),
                new Participant("mair", null, null, null));
        String expenseName = "Taxi";
        Date date = new Date(2022, 3, 1);


        Expense newExpense = new Expense(user2, amount,"EUR", beneficiaries, expenseName, date,
                new Tag("food", "red", 1L));


        assertEquals(e.hashCode(), newExpense.hashCode());
    }

    @Test
    void testNotEquals() {
        Participant user1 = new Participant("mreia", null, null, null);
        double amount = 20.00;
        List<Participant> beneficiaries = List.of(new Participant
                        ("jake", null, null, null),
                new Participant("mair", null, null, null));
        String expenseName = "Taxi";
        Date date = new Date(2022, 3, 1);

        Expense newExpense = new Expense(user1, amount,"EUR", beneficiaries, expenseName, date, new Tag());

        assertNotEquals(e, newExpense);
    }

    @Test
    void getCurrency() {
        assertEquals("EUR", e.getCurrency());
    }

    @Test
    void setCurrency() {
        e.setCurrency("USD");
        assertEquals("USD", e.getCurrency());
    }
}