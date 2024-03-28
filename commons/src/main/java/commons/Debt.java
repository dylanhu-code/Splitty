package commons;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Represents a debt between two users related to a specific event.
 */
@Entity
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long debtId;

    @ManyToOne()
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private Participant user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private Participant user2;
    private double amount;
    private boolean settled;

    /**
     * Constructs a Debt object.
     *
     * @param event  The event associated with the debt.
     * @param user1  The user who owes the debt.
     * @param user2  The user to whom the debt is owed.
     * @param amount The amount of the debt.
     */
    public Debt(Event event, Participant user1, Participant user2, double amount) {
        this.event = event;
        this.user1 = user1;
        this.user2 = user2;
        this.amount = amount;
        this.settled = false;
    }

    /**
     * Gets the event associated with the debt.
     *
     * @return The event associated with the debt.
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Gets the user who owes the debt.
     *
     * @return The user who owes the debt.
     */
    public Participant getDebtor() {
        return user1;
    }

    /**
     * Gets the user to whom the debt is owed.
     *
     * @return The user to whom the debt is owed.
     */
    public Participant getCreditor() {
        return user2;
    }

    /**
     * Gets the amount of the debt.
     *
     * @return The amount of the debt.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Checks if the debt is settled.
     *
     * @return True if the debt is settled, false otherwise.
     */
    public boolean isSettled() {
        return settled;
    }

    /**
     * Sets the debt as settled.
     *
     * @throws IllegalStateException If the debt is already settled.
     */
    public void settleDebt() {
        if (settled) {
            throw new IllegalStateException("Debt is already settled");
        }
        settled = true;
    }

    /**
     * Pays a certain amount of the debt.
     *
     * @param amountPaid The amount to be paid.
     * @throws IllegalStateException    If the debt is already settled.
     * @throws IllegalArgumentException If the amount paid is negative.
     */
    public void payDebt(double amountPaid) {
        if (settled) {
            throw new IllegalStateException("Debt is already settled");
        }

        if (amountPaid < 0) {
            throw new IllegalArgumentException("Amount paid cannot be negative");
        }

        if (amountPaid >= amount) {
            amount = 0;
            settleDebt();
        } else {
            amount -= amountPaid;
        }
    }

    /**
     * Checks if this debt is equal to another object.
     *
     * @param o The object to compare.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Debt debt = (Debt) o;
        return Double.compare(debt.amount, amount) == 0 &&
                user1.equals(debt.user1) &&
                user2.equals(debt.user2);
    }

    /**
     * Generates a hash code for this debt.
     *
     * @return The hash code for this debt.
     */
    @Override
    public int hashCode() {
        return Objects.hash(user1, user2, amount);
    }

    /**
     * Returns a string representation of the debt.
     *
     * @return A string representation of the debt.
     */
    @Override
    public String toString() {
        return "Debt{" +
                "debtor=" + user1 +
                ", creditor=" + user2 +
                ", amount=" + amount +
                '}';
    }
}