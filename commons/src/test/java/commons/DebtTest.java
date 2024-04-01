package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DebtTest {
    private Event event;
    private Participant user1;
    private Participant user2;
    private Debt debt;

    @BeforeEach
    public void setUp() {
        event = new Event("Holiday");
        user1 = new Participant("Peter", "mm@gmail.com", "893494", null);
        user2 = new Participant("Juan", "dklajd@gmail.com", null, null);
        debt = new Debt(event, user1, user2, 50.0); // Example debt with an amount of 50.0
    }

    @Test
    void testDebtGettersAndSettlement() {
        assertEquals(event, debt.getEvent());
        assertEquals(user1, debt.getUser1());
        assertEquals(user2, debt.getUser2());
        assertEquals(50.0, debt.getAmount());
        assertFalse(debt.isSettled());

        debt.settleDebt();

        assertTrue(debt.isSettled());
    }

    @Test
    void testSettleWhenSettled() {
        debt.settleDebt();
        assertThrows(IllegalStateException.class, () -> debt.settleDebt());
    }

    @Test
    void testDebtPayment() {
        debt.payDebt(30.0);

        assertFalse(debt.isSettled());
        assertEquals(20.0, debt.getAmount());

        debt.payDebt(20.0);

        assertTrue(debt.isSettled());
        assertEquals(0.0, debt.getAmount());
    }

    @Test
    void testPayNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> debt.payDebt(-10.0));
        assertFalse(debt.isSettled());
        assertEquals(50.0, debt.getAmount());
    }

    @Test
    void testPayWhileAlreadySettled() {
        debt.payDebt(50.0);

        assertThrows(IllegalStateException.class, () -> debt.payDebt(10.0));
        assertTrue(debt.isSettled());
        assertEquals(0.0, debt.getAmount());
    }


    @Test
    void testDebtEquality() {
        Debt debtCopy = new Debt(event, user1, user2, 50.0);
        assertEquals(debt, debtCopy);
    }

    @Test
    void testDebtInequality() {
        Debt debt2 = new Debt(event, user1, user2, 100.0);
        assertNotEquals(debt, debt2);
    }

    @Test
    public void testToString() {
        String expectedString = "Debt{debtor=" + user1 + ", creditor=" + user2 + ", amount=50.0}";
        assertEquals(expectedString, debt.toString());
    }

    @Test
    public void testHashCodeEquality() {
        Debt debtCopy = new Debt(event, user1, user2, 50.0);
        assertEquals(debt.hashCode(), debtCopy.hashCode());
    }

    @Test
    void testDebtHashCodeInequality() {
        Debt debt2 = new Debt(event, user1, user2, 100.0);
        assertNotEquals(debt.hashCode(), debt2.hashCode());
    }
}