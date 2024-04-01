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
     * @param user1  The user who owes the debt.
     * @param user2  The user to whom the debt is owed.
     * @param amount The amount of the debt.
     */
    public Debt( Participant user1, Participant user2, double amount) {
        this.user1 = user1;
        this.user2 = user2;
        this.amount = amount;
        this.settled = false;
    }

    public Debt() {

    }

    public long getDebtId() {
        return debtId;
    }

    public void setDebtId(long debtId) {
        this.debtId = debtId;
    }

    public Participant getUser1() {
        return user1;
    }

    public void setUser1(Participant user1) {
        this.user1 = user1;
    }

    public Participant getUser2() {
        return user2;
    }

    public void setUser2(Participant user2) {
        this.user2 = user2;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isSettled() {
        return settled;
    }

    public void setSettled(boolean settled) {
        this.settled = settled;
    }

    /**
     * Pays a certain amount of the debt.
     *
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
            setSettled(true);
        } else {
            amount -= amountPaid;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Debt debt = (Debt) o;

        if (debtId != debt.debtId) return false;
        if (Double.compare(amount, debt.amount) != 0) return false;
        if (settled != debt.settled) return false;
        if (!Objects.equals(user1, debt.user1)) return false;
        return Objects.equals(user2, debt.user2);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (debtId ^ (debtId >>> 32));
        result = 31 * result + (user1 != null ? user1.hashCode() : 0);
        result = 31 * result + (user2 != null ? user2.hashCode() : 0);
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (settled ? 1 : 0);
        return result;
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